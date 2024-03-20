package com.hyl.zhanmaoj.model.vo;

import com.hyl.zhanmaoj.common.PageRequest;
import com.hyl.zhanmaoj.model.entity.ChoiceQuestionSubmit;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

@Data
public class ChoiceQuestionSubmitVO extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 答案 0 A 1 B 2 C 3 D
     */
    private Integer answer;

    /**
     * 判题状态（0 - 错误、1 - 正确）
     */
    private Integer status;

    /**
     * 题目 id
     */
    private Long choiceQuestionId;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 试卷 id
     */
    private Long testId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 提交用户信息
     */
    private UserVO userVO;

    /**
     * 对应题目信息
     */
    private ChoiceQuestionVO choiceQuestionVO;

    /**
     * 包装类转对象
     *
     * @param choiceQuestionSubmitVO
     * @return
     */
    public static ChoiceQuestionSubmit voToObj(ChoiceQuestionSubmitVO choiceQuestionSubmitVO) {
        if (choiceQuestionSubmitVO == null) {
            return null;
        }
        ChoiceQuestionSubmit choiceQuestionSubmit = new ChoiceQuestionSubmit();
        BeanUtils.copyProperties(choiceQuestionSubmitVO, choiceQuestionSubmit);
        return choiceQuestionSubmit;
    }

    /**
     * 对象转包装类
     *
     * @param choiceQuestionSubmit
     * @return
     */
    public static ChoiceQuestionSubmitVO objToVo(ChoiceQuestionSubmit choiceQuestionSubmit) {
        if (choiceQuestionSubmit == null) {
            return null;
        }
        ChoiceQuestionSubmitVO choiceQuestionSubmitVO = new ChoiceQuestionSubmitVO();
        BeanUtils.copyProperties(choiceQuestionSubmit, choiceQuestionSubmitVO);
        return choiceQuestionSubmitVO;
    }

    private static final long serialVersionUID = 1L;
}
