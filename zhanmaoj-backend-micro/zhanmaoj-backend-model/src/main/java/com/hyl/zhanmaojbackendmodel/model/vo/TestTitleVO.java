package com.hyl.zhanmaojbackendmodel.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TestTitleVO implements Serializable {
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
     * 总分
     */
    private Integer totalScore;

    /**
     * 编程问题信息列表
     */
    private List<QuestionTitleVO> questionTitleVOS;

    /**
     * 判断题信息列表
     */
    private List<TrueOrFalseTitleVO> trueOrFalseTitleVOS;

    /**
     * 选择题信息列表
     */
    private List<ChoiceQuestionTitleVO> choiceQuestionTitleVOS;


    private static final long serialVersionUID = 1L;
}
