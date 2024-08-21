package com.king.friends.service;

import com.king.friends.model.domain.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.king.friends.model.domain.User;
import com.king.friends.model.dto.TeamQuery;
import com.king.friends.model.request.TeamJoinRequest;
import com.king.friends.model.request.TeamQuitRequest;
import com.king.friends.model.request.TeamUpdateRequest;
import com.king.friends.model.vo.TeamUserVO;
import java.util.List;

/**
* @author 13615
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2024-07-14 15:14:51
*/
public interface TeamService extends IService<Team> {

    /**
     * 创建用户
     * @param team
     * @param loginUser
     * @return
     */
    long addTeam(Team team, User loginUser);

    /**
     * 搜索队伍
     * @param teamQuery
     * @param isAdmin
     * @return
     */
    List<TeamUserVO> listTeam(TeamQuery teamQuery,boolean isAdmin);

    /**
     * 更新队伍
     * @param teamUpdateRequest
     * @param userLogin
     * @return
     */

    /**
     * 更新队伍
     * @param teamUpdateRequest
     * @param userLogin
     * @return
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest,User userLogin);

    /**
     * 加入队伍
     * @param teamJoinRequest
     * @param loginUser
     * @return
     */

    boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);

    /**
     * 退出队伍
     * @param teamQuitRequest
     * @param loginUser
     * @return
     */

    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    /**
     *  解散队伍
     * @param id
     * @param loginUser
     * @return
     */

    boolean deleteTeam(long id, User loginUser);
}
