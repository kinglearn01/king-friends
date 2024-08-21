package com.king.friends.common;

import java.io.Serializable;
import lombok.Data;

/**
 * ClassName:PageRequest
 * Description:
 *
 * @Author:kinglearn
 * @Create2024/7/14 16:07
 * @version1.0
 */
@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = 7132556857756071421L;
    /**
     * 页面大小
     */
    protected int pageSize =10;
    /**
     * 当前第几页
     */
    protected int pageNum=1;
}
