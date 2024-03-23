package com.hyl.zhanmaoj.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionTestAddVO implements Serializable {

    private Long questionId;

    private Integer type;

    private Integer score;


    private static final long serialVersionUID = 1L;
}
