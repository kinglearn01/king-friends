package com.king.friends.model.request;

import java.io.Serializable;
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
public class TeamJoinRequest implements Serializable {

    private static final long serialVersionUID = 7014496409878694496L;


    /**
     * id
     */
    private Long teamId;

    /**
     * 密码
     */
    private String password;


}
