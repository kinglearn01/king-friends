package com.king.friends.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.king.friends.common.PageRequest;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ClassName:TeamQuery
 * Description:队伍查询封装类
 *
 * @Author:kinglearn
 * @Create2024/7/14 15:47
 * @version1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TeamQuery extends PageRequest {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * id 列表
     */
    private List<Long> idList;

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
     * 搜索关键词（同时对队伍名称和描述搜索）
     */
    private String searchText;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 0 - 公开，1 - 私有 2 - 加密
     */
    private Integer status;




    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
