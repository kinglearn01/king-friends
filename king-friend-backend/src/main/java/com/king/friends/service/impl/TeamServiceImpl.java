package com.king.friends.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.king.friends.common.ErrorCode;
import com.king.friends.exception.BusinessException;
import com.king.friends.model.comenum.TeamStatusEnum;
import com.king.friends.model.domain.Team;
import com.king.friends.model.domain.User;
import com.king.friends.model.domain.UserTeam;
import com.king.friends.model.dto.TeamQuery;
import com.king.friends.model.request.TeamJoinRequest;
import com.king.friends.model.request.TeamQuitRequest;
import com.king.friends.model.request.TeamUpdateRequest;
import com.king.friends.model.vo.TeamUserVO;
import com.king.friends.model.vo.UserVO;
import com.king.friends.service.TeamService;
import com.king.friends.mapper.TeamMapper;
import com.king.friends.service.UserService;
import com.king.friends.service.UserTeamService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @author 13615
* @description 针对表【team(队伍)】的数据库操作Service实现
* @createDate 2024-07-14 15:14:51
*/
@Service
@Slf4j
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService{
    @Resource
    private UserTeamService userTeamService;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private UserService userService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long addTeam(Team team, User loginUser) {
//        判断请求参数是否为空
        if (team==null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
//        判断是否登录，未登录不允许创建
        if(loginUser==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        final long userId = loginUser.getId();
//        校验信息
//        校验队伍人数
        int maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if (maxNum<1 || maxNum>20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍人数不满足要求");
        }
//        校验队伍标题
        String name = team.getName();
        if (StringUtils.isBlank(name)||name.length()>20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍标题过长");
        }
//        描述
         String description = team.getDescription();
        if (StringUtils.isNotBlank(description)&&description.length()>512){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍描述过长");
        }
//        状态
       int status = Optional.ofNullable(team.getStatus()).orElse(0);
       TeamStatusEnum enumByValue = TeamStatusEnum.getEnumByValue(status);
        if (enumByValue==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍状态出现问题");
        }
//        判断是否加密 设置密码
        String password = team.getPassword();
        if (TeamStatusEnum.SECRET.equals(enumByValue)){
            if((StringUtils.isBlank(password)) || (password.length()>32)){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码设置不正确");
            }
        }
//        校验时间
        Date expireTime = team.getExpireTime();
        if (new Date().after(expireTime)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"创建超时");
        }
//        校验队伍
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",userId);
        long hasTeamNum = this.count(queryWrapper);
        if (hasTeamNum>=5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户最多创建5个队伍");
        }
//        插入信息到队伍表
        team.setId(null);
        team.setUserId(userId);
         boolean result = this.save(team);
        if (!result||team.getId()==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"插入队伍信息失败");
        }
//        插入用户到队伍关系表
         UserTeam userTeam = new UserTeam();

         userTeam.setUserId(userId);
         userTeam.setTeamId(team.getId());
         userTeam.setCreateTime(new Date());
         result=userTeamService.save(userTeam);
         if (!result){
             throw new BusinessException(ErrorCode.PARAMS_ERROR,"创建队伍失败");
         }
        return team.getId();
    }

    @Override
    public List<TeamUserVO> listTeam(TeamQuery teamQuery,boolean isAdmin) {
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        if (teamQuery!=null){
//            根据id查询
            Long id= teamQuery.getId();
            if (id!=null && id>0){
                queryWrapper.eq("id",id);
            }
            List<Long> idList = teamQuery.getIdList();
            if (CollectionUtils.isNotEmpty(idList)){
                queryWrapper.in("id",idList);
            }
            String searchText = teamQuery.getSearchText();
            if (StringUtils.isNotBlank(searchText)){
                queryWrapper.and(qw->qw.like("name",searchText).or().like("description",searchText));
            }
//            根据队伍名字查询
            String name = teamQuery.getName();
            if (StringUtils.isNotBlank(name)){
                queryWrapper.like("name",name);
            }
//            根据描述查询
            String description = teamQuery.getDescription();
            if (StringUtils.isNotBlank(description)){
                queryWrapper.like("description",description);
            }
//            根据最大数量查询相等的
            Integer maxNum = teamQuery.getMaxNum();
            if (maxNum!=null && maxNum>0){
                queryWrapper.eq("maxNum",maxNum);
            }
//            根据创建人查询
            Long userId = teamQuery.getUserId();
            if (userId!=null&&userId>0){
                queryWrapper.eq("userId",userId);
            }
//            根据状态查询
            Integer status = teamQuery.getStatus();
           TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
           if (statusEnum==null ){
               statusEnum=TeamStatusEnum.PUBLIC;
           }
            if (!isAdmin && statusEnum.equals(TeamStatusEnum.PRIVATE)){
                throw new BusinessException(ErrorCode.NO_AUTH);
            }
            queryWrapper.eq("status",statusEnum.getValue());
        }
//        不展示已过期的队伍
        queryWrapper.and(qw->qw.gt("expireTime",new Date()).or().isNull("expireTime"));
        List<Team> teamList = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(teamList)){
            return new ArrayList<>();
        }
//        关联查询创建人的用户信息
        List<TeamUserVO> teamUserVOList = new ArrayList<>();
        for (Team team:teamList){
           Long userId = team.getUserId();
           if (userId == null){
               continue;
           }
             User user = userService.getById(userId);
            TeamUserVO teamUserVO = new TeamUserVO();
            BeanUtils.copyProperties(team,teamUserVO);
//            脱敏用户信息
            if (user!=null){
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(user,userVO);
                teamUserVO.setCreateUser(userVO);
            }
            teamUserVOList.add(teamUserVO);
        }
        return teamUserVOList;
    }

    /**
     * 更新队伍
     * @param teamUpdateRequest
     * @return
     */
    @Override
    public boolean updateTeam(TeamUpdateRequest teamUpdateRequest,User userLogin) {
        if (teamUpdateRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = teamUpdateRequest.getId();
        if (id==null || id<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
         Team oldTeam = this.getById(id);
        if (oldTeam==null){
            throw new BusinessException(ErrorCode.NULL_ERROR,"队伍不存在");
        }
        //只有队伍管理员和创建者才能修改队伍
        if (oldTeam.getUserId() != userLogin.getId() && !userService.isAdmin(userLogin)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(teamUpdateRequest.getStatus());
        if (statusEnum.equals(TeamStatusEnum.SECRET)){
            if (StringUtils.isBlank(teamUpdateRequest.getPassword())){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"加密房间必须设置密码");
            }
        }
        Team updateTeam = new Team();
        BeanUtils.copyProperties(teamUpdateRequest,updateTeam);
        return this.updateById(updateTeam);
    }

    /**
     * 加入队伍
     * @param teamJoinRequest
     * @return
     */
    @Override
    public boolean joinTeam(TeamJoinRequest teamJoinRequest,User loginUser) {
        if (teamJoinRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long teamId = teamJoinRequest.getTeamId();
        Team team = getTeamById(teamId);
        Date expireTime = team.getExpireTime();
        if (team.getExpireTime() != null && expireTime.before(new Date())){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍已经过期");
        }
        Integer status = team.getStatus();
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
        if (TeamStatusEnum.PRIVATE.equals(statusEnum)){
            throw new BusinessException(ErrorCode.NULL_ERROR,"禁止加入私有队伍");
        }
        String password = teamJoinRequest.getPassword();
        if (statusEnum.equals(TeamStatusEnum.SECRET)){
                if (StringUtils.isBlank(password) || !team.getPassword().equals(password)){
                    throw new BusinessException(ErrorCode.NULL_ERROR,"密码错误");
            }
        }
//        该用户已加入的队伍数量
        Long userId = loginUser.getId();
        //只有一个线程能获取到锁
        RLock lock = redissonClient.getLock("user:join_team");
        try {
            //抢到锁并执行
            while (true){
                if (lock.tryLock(0,-1, TimeUnit.MILLISECONDS)){
                    System.out.println("getLock"+Thread.currentThread().getId());

                    QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
                    userTeamQueryWrapper.eq("userId", userId);

                    long hasJoinNum = userTeamService.count(userTeamQueryWrapper);
                    if (hasJoinNum>5){
                        throw new BusinessException(ErrorCode.PARAMS_ERROR,"最多加入5个队伍");
                    }
//        判断是否重复加入
                    userTeamQueryWrapper = new QueryWrapper<>();
                    userTeamQueryWrapper.eq("userId",userId);
                    userTeamQueryWrapper.eq("teamId", teamId);
                    long hasUserJoinTeam = userTeamService.count(userTeamQueryWrapper);
                    if (hasUserJoinTeam>0){
                        throw new BusinessException(ErrorCode.PARAMS_ERROR,"请不要重复加入");
                    }
//        已近加入队伍的人数
                    long teamHasJoinNum = this.countTeamUserByTeamId(teamId);
                    if (teamHasJoinNum>=team.getMaxNum()){
                        throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍已满");
                    }
                    UserTeam userTeam = new UserTeam();
                    userTeam.setUserId(userId);
                    userTeam.setTeamId(teamId);
                    userTeam.setJoinTime(new Date());
                    return userTeamService.save(userTeam);
                }
            }
        } catch (InterruptedException e) {
            log.error("error",e);
            return false;
        }finally {
            if (lock.isHeldByCurrentThread()){
                System.out.println("unlock"+Thread.currentThread().getId());
                lock.unlock();
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser) {
        if (teamQuitRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long teamId = teamQuitRequest.getTeamId();
        final Team team = getTeamById(teamId);
        long userId = loginUser.getId();
        UserTeam queryUserTeam = new UserTeam();
        queryUserTeam.setTeamId(teamId);
        queryUserTeam.setUserId(userId);
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>(queryUserTeam);
         long count = userTeamService.count(queryWrapper);
         if (count==0){
             throw new BusinessException(ErrorCode.PARAMS_ERROR,"未加入队伍");
         }
         long teamHasJoinNum  = this.countTeamUserByTeamId(teamId);
         //队伍只剩一人，解散
        if (teamHasJoinNum==1){
            //删除队伍和加入队伍的关系
            this.deleteTeam(teamId,loginUser);
        }else {
            //如果还有其他人，判断是否为队长
            if (team.getUserId()==userId){
//                把队伍转移给最早加入的用户
//                查询已加入队伍的所有用户和加入时间
               QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
               userTeamQueryWrapper.eq("teamId",teamId);
               userTeamQueryWrapper.last("order by id asc limit 2");
               List<UserTeam> list = userTeamService.list(userTeamQueryWrapper);
               if (CollectionUtils.isEmpty(list)||list.size()<=1){
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR);
               }
               UserTeam nextUserTeam = list.get(1);
               Long nextUserTeamUserId = nextUserTeam.getUserId();
               //更新当前队伍队长
               Team updateTeam = new Team();
               updateTeam.setId(teamId);
               updateTeam.setUserId(nextUserTeamUserId);
               boolean result = this.updateById(updateTeam);
               if (!result) {
                   throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新对队长失败");
               }
            }
        }
        //移除关系
        return userTeamService.remove(queryWrapper);
    }

    /**
     * 根据id获取队伍信息
     * @param teamId
     * @return
     */
    private Team getTeamById(Long teamId) {
        if (teamId == null|| teamId <=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = this.getById(teamId);
        if (team == null){
            throw new BusinessException(ErrorCode.NULL_ERROR,"队伍不存在");
        }
        return team;
    }

    @Override
//    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTeam(long id, User loginUser) {
//        校验队伍是否存在
        Team team = getTeamById(id);
        long teamId = team.getId();
//        校验你是否为队伍队长
        if(!team.getUserId().equals(loginUser.getId())){
            throw new BusinessException(ErrorCode.NO_AUTH,"无访问权限");
        }
//        移除所有加入队伍的关联信息
        QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
        userTeamQueryWrapper.eq("teamId",teamId);
         boolean result = userTeamService.remove(userTeamQueryWrapper);
         if (!result){
             throw new BusinessException(ErrorCode.SYSTEM_ERROR,"删除队伍失败");
         }
//        删除队伍
        return this.removeById(teamId);
    }

    /**
     * 获取某队伍当前人数
     * @param teamId
     * @return
     */
    private long countTeamUserByTeamId(long teamId){
       QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
       userTeamQueryWrapper.eq("teamId",teamId);
        return userTeamService.count(userTeamQueryWrapper);
    }

}




