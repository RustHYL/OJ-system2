package com.hyl.zhanmaojbackendmodel.model.dto.trueorfalse;


import com.hyl.zhanmaojbackendcommon.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询请求
 *

 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TrueOrFalseQueryAdminRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 内容
     */
    private String title;

    /**
     * 标签列表
     */
    private String tags;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}