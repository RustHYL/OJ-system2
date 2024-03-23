package com.hyl.zhanmaoj.model.dto.test;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 创建请求
 *

 */
@Data
public class TestAddRequest implements Serializable {


    /**
     * 标题
     */
    private String title;

    /**
     * 状态 0-表示公开，1-表示私有，2-表示加密
     */
    private Integer status;

    /**
     * 密码
     */
    private String password;

    /**
     * 内容
     */
    private String content;

    /**
     * 开始时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /**
     * 过期时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expiredTime;

    /**
     * 做题时间
     */
    private Long examTime;


    private Integer trueOrFalseNum;

    private Integer trueOrFalsePerScore;

    private Integer choiceQuestionNum;

    private Integer choiceQuestionPerScore;

    private String questionList;

    private static final long serialVersionUID = 1L;
}