package com.hyl.zhanmaoj.model.vo;

import cn.hutool.json.JSONUtil;
import com.hyl.zhanmaoj.model.entity.ChoiceQuestion;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ChoiceQuestionTitleVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 内容
     */
    private String content;

    /**
     * 类型
     */
    private Integer type;

    private static final long serialVersionUID = 1L;

}
