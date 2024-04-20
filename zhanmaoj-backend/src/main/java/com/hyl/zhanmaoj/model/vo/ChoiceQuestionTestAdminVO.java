package com.hyl.zhanmaoj.model.vo;

import cn.hutool.json.JSONUtil;
import com.hyl.zhanmaoj.model.entity.ChoiceQuestion;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ChoiceQuestionTestAdminVO implements Serializable {

    /**
     * id
     */
    private Long id;

    private Long questionId;

    /**
     * 内容
     */
    private String title;

    /**
     * 标签列表
     */
    private String tags;

    /**
     * 分数
     */
    private Integer score;

    /**
     * 答案
     */
    private Integer answer;

    private String optionA;

    private String optionB;

    private String optionC;

    private String optionD;
}
