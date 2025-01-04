package com.hyl.zhanmaojbackendmodel.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TestVO implements Serializable {
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
     * 考试时间
     */
    private Long examTime;

    /**
     * 总分
     */
    private Integer totalScore;

    /**
     * 编程问题信息列表
     */
    private List<QuestionTestDetailVO> questionTestDetailVOS;

    /**
     * 判断题信息列表
     */
    private List<TrueOrFalseTestDetailVO> trueOrFalseTestDetailVOS;

    /**
     * 选择题信息列表
     */
    private List<ChoiceQuestionTestDetailVO> choiceQuestionTestDetailVOS;


    private static final long serialVersionUID = 1L;
}
