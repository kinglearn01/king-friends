package com.king.friends.common;

import java.io.Serializable;
import lombok.Data;

/**
 * ClassName:PageRequest
 * Description:删除请求参数
 *
 * @Author:kinglearn
 * @Create2024/7/14 16:07
 * @version1.0
 */
@Data
public class DeleteRequest implements Serializable {

    private static final long serialVersionUID = 7132556857756071421L;
    private long   id;
}
