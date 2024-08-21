package com.king.friends.model.request;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * ClassName:TeamAddRequest
 * Description:
 *
 * @Author:kinglearn
 * @Create2024/7/15 9:05
 * @version1.0
 */
@Data
public class TeamUpdateRequest implements Serializable {

    private static final long serialVersionUID = 7014496409878694496L;


    /**
     * id
     */

    private Long id;
    /**
     * 队伍名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;


    /**
     * 过期时间
     */
    private Date expireTime;



    /**
     * 0 - 公开，1 - 私有 2 - 加密
     */
    private Integer status;

    /**
     * 密码
     */
    private String password;


}
