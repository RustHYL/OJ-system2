package com.hyl.zhanmaoj.model.vo;

import cn.hutool.json.JSONUtil;
import com.hyl.zhanmaoj.model.dto.question.JudgeConfig;
import com.hyl.zhanmaoj.model.entity.Question;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 题目封装类
 * @TableName question
 */
@Data
public class QuestionTitleVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 类型
     */
    private Integer type;


    private static final long serialVersionUID = 1L;
}