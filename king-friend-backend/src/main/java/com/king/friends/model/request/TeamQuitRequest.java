package com.king.friends.model.request;

import java.io.Serializable;
import lombok.Data;

/**
 * ClassName:TeamQuiteRequeat
 * Description:用户退出队伍
 *
 * @Author:kinglearn
 * @Create2024/7/22 9:13
 * @version1.0
 */
@Data
public class TeamQuitRequest implements Serializable {

    private static final long serialVersionUID = -3836209161351571244L;
    private Long teamId;
}
