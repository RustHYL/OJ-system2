package com.hyl.zhanmaojbackendmodel.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionTestAddVO implements Serializable {

    private Long id;

    private Integer type;

    private Integer score;


    private static final long serialVersionUID = 1L;
}
