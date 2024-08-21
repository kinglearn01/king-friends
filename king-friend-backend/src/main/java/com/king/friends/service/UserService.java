package com.king.friends.service;

import com.king.friends.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.king.friends.model.request.UserRegisterRequest;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 *
 * * */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);


    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);

    /**
     * 根据标签搜索用户
     *
     * @param tagList 用户要拥有的标签
     * @return
     */
    List<User> searchUserByTag(List<String> tagList);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    int updateUser(User user,User loginUser);

    /**
     * 获取当前用户登录状态
     * @param
     * @return
     */
   User getLoginUser(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param loginUser
     * @return
     */
    boolean isAdmin(User loginUser);

    /**
     * 用户标签匹配
     *
     * @param num
     * @param loginUser
     * @return
     */

    List<User> matchUsers(long num, User loginUser);
}
