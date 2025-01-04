package com.hyl.zhanmaojbackendmodel.model.dto.choicequestion;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新请求
 *

 */
@Data
public class ChoiceQuestionEditRequest implements Serializable {

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
    private List<String> tags;

    /**
     * 选项 A
     */
    private String optionA;

    /**
     * 选项 B
     */
    private String optionB;

    /**
     * 选项 C
     */
    private String optionC;

    /**
     * 选项 D
     */
    private String optionD;

    /**
     * 题目答案
     */
    private Integer answer;


    private static final long serialVersionUID = 1L;
}