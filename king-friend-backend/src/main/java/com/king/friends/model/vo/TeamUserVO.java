package com.king.friends.model.vo;

import com.baomidou.mybatisplus.annotation.TableLogic;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * ClassName:TeamUserVO
 * Description:队伍和用户信息封装类
 *
 * @Author:kinglearn
 * @Create2024/7/15 14:25
 * @version1.0
 */
@Data
public class TeamUserVO implements Serializable {
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
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 0 - 公开，1 - 私有 2 - 加密
     */
    private Integer status;



    /**
     * 创建时间
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;
    /**
     * 创建人列表
     */

    UserVO createUser;
    /**
     * 是否加入队伍
     *
     */
    private Boolean hasJoin=false;

    private static final long serialVersionUID = 4816454757289595018L;
}
