package com.king.friends.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.king.friends.model.domain.UserTeam;
import com.king.friends.service.UserTeamService;
import com.king.friends.mapper.UserTeamMapper;
import org.springframework.stereotype.Service;

/**
* @author 13615
* @description 针对表【user_team(用户队伍关系)】的数据库操作Service实现
* @createDate 2024-07-14 15:16:05
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService{

}




