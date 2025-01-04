package com.hyl.zhanmaojbackendmodel.model.vo;

import cn.hutool.json.JSONUtil;
import com.hyl.zhanmaojbackendcommon.common.PageRequest;
import com.hyl.zhanmaojbackendmodel.model.codesandbox.JudgeInfo;
import com.hyl.zhanmaojbackendmodel.model.entity.QuestionWrong;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 错题
 * @TableName question_submit
 */
@Data
public class QuestionWrongVO extends PageRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 判题信息（json 对象）
     */
    private JudgeInfo judgeInfo;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 提交用户id
     */
    private Long submitId;

    /**
     * 题目
     */
    private String title;

    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 包装类转对象
     *
     * @param questionWrongVO
     * @return
     */
    public static QuestionWrong voToObj(QuestionWrongVO questionWrongVO) {
        if (questionWrongVO == null) {
            return null;
        }
        QuestionWrong questionWrong = new QuestionWrong();
        BeanUtils.copyProperties(questionWrongVO, questionWrong);
        List<String> tagList = questionWrongVO.getTags();
        if (tagList != null) {
            questionWrong.setTags(JSONUtil.toJsonStr(tagList));
        }
        JudgeInfo judgeInfo = questionWrongVO.getJudgeInfo();
        if (judgeInfo != null) {
            questionWrong.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        }
        return questionWrong;
    }

    /**
     * 对象转包装类
     *
     * @param questionWrong
     * @return
     */
    public static QuestionWrongVO objToVo(QuestionWrong questionWrong) {
        if (questionWrong == null) {
            return null;
        }
        QuestionWrongVO questionWrongVO = new QuestionWrongVO();
        BeanUtils.copyProperties(questionWrong, questionWrongVO);
        List<String> tagList = JSONUtil.toList(questionWrong.getTags(), String.class);
        JudgeInfo judgeInfo = JSONUtil.toBean(questionWrong.getJudgeInfo(), JudgeInfo.class);
        questionWrongVO.setTags(tagList);
        questionWrongVO.setJudgeInfo(judgeInfo);
        return questionWrongVO;
    }

    private static final long serialVersionUID = 1L;
}
