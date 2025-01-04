package com.hyl.zhanmaojbackendmodel.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TestSubmitFinalDetailVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 题目数量
     */
    private Integer questionNum;

    /**
     * 得分
     */
    private Integer score;

    /**
     * 开始做卷时间
     */
    private Date beginTime;

    /**
     * 结束做卷时间
     */
    private Date endTime;


    private static final long serialVersionUID = 1L;
}
