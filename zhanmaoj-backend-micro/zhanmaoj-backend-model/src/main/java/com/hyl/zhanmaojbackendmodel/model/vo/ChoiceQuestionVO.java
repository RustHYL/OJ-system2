package com.hyl.zhanmaojbackendmodel.model.vo;

import cn.hutool.json.JSONUtil;

import com.hyl.zhanmaojbackendmodel.model.entity.ChoiceQuestion;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ChoiceQuestionVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 内容
     */
    private String title;

    /**
     * 题目提交数
     */
    private Integer submitNum;

    /**
     * 题目通过数
     */
    private Integer acceptedNum;

    /**
     * 标签列表
     */
    private List<String> tags;

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


    /**
     * 创建人信息
     */
    private UserVO userVO;


    /**
     * 包装类转对象
     *
     * @param choiceQuestionVO
     * @return
     */
    public static ChoiceQuestion voToObj(ChoiceQuestionVO choiceQuestionVO) {
        if (choiceQuestionVO == null) {
            return null;
        }
        ChoiceQuestion choiceQuestion = new ChoiceQuestion();
        BeanUtils.copyProperties(choiceQuestionVO, choiceQuestion);
        List<String> tagList = choiceQuestionVO.getTags();
        if (tagList != null) {
            choiceQuestion.setTags(JSONUtil.toJsonStr(tagList));
        }
        return choiceQuestion;
    }

    /**
     * 对象转包装类
     *
     * @param choiceQuestion
     * @return
     */
    public static ChoiceQuestionVO objToVo(ChoiceQuestion choiceQuestion) {
        if (choiceQuestion == null) {
            return null;
        }
        ChoiceQuestionVO choiceQuestionVO = new ChoiceQuestionVO();
        BeanUtils.copyProperties(choiceQuestion, choiceQuestionVO);
        List<String> tagList = JSONUtil.toList(choiceQuestion.getTags(), String.class);
        choiceQuestionVO.setTags(tagList);
        return choiceQuestionVO;
    }
}
