package com.king.friends.model.request;

import java.util.List;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * * */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 校验密码
     */
    private String checkPassword;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 电话
     */
    private String phone;
    /**
     * 标签列表 json
     */
    private List<String> tags;
    /**
     * 性别
     */
    private Integer gender;

}

