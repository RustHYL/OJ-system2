package com.hyl.zhanmaojbackendmodel.model.dto.testSubmit;

import com.hyl.zhanmaojbackendcommon.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TestSubmitQueryRequest extends PageRequest implements Serializable {


    /**
     * 提交状态
     */
    private Integer status;

    /**
     * 试卷 id
     */
    private Long testId;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;


    private static final long serialVersionUID = 1L;
}