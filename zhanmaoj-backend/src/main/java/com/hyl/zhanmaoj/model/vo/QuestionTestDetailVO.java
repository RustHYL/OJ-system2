package com.hyl.zhanmaoj.model.vo;

import cn.hutool.json.JSONUtil;
import com.hyl.zhanmaoj.model.dto.question.JudgeConfig;
import com.hyl.zhanmaoj.model.entity.Question;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class QuestionTestDetailVO implements Serializable {

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
     * 标签列表
     */
    private List<String> tags;

    /**
     * 题目提交数
     */
    private Integer submitNum;

    /**
     * 题目通过数
     */
    private Integer acceptedNum;

    /**
     * 判题配置
     */
    private JudgeConfig judgeConfig;

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
     * 分数
     */
    private Integer score;


    /**
     * 包装类转对象
     *
     * @param questionVO
     * @return
     */
    public static Question voToObj(QuestionTestDetailVO questionVO) {
        if (questionVO == null) {
            return null;
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionVO, question);
        List<String> tagList = questionVO.getTags();
        if (tagList != null) {
            question.setTags(JSONUtil.toJsonStr(tagList));
        }
        JudgeConfig judgeConfig = questionVO.getJudgeConfig();
        if (judgeConfig != null) {
            question.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig));
        }
        return question;
    }

    /**
     * 对象转包装类
     *
     * @param question
     * @return
     */
    public static QuestionTestDetailVO objToVo(Question question) {
        if (question == null) {
            return null;
        }
        QuestionTestDetailVO questionTestDetailVO = new QuestionTestDetailVO();
        BeanUtils.copyProperties(question, questionTestDetailVO);
        List<String> tagList = JSONUtil.toList(question.getTags(), String.class);
        questionTestDetailVO.setTags(tagList);
        String judgeConfigStr = question.getJudgeConfig();
        questionTestDetailVO.setJudgeConfig(JSONUtil.toBean(judgeConfigStr, JudgeConfig.class));
        return questionTestDetailVO;
    }

    private static final long serialVersionUID = 1L;
}
