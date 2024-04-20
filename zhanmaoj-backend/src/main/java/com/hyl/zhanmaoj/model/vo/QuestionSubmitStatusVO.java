package com.hyl.zhanmaoj.model.vo;

import com.hyl.zhanmaoj.model.entity.QuestionSubmit;
import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionSubmitStatusVO implements Serializable {
    /**
     * status
     */
    private String status;

    /**
     * 标题
     */
    private QuestionSubmitVO result;


    private static final long serialVersionUID = 1L;
}
