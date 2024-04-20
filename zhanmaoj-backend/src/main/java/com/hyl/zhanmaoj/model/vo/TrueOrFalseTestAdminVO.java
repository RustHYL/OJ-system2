package com.hyl.zhanmaoj.model.vo;

import cn.hutool.json.JSONUtil;
import com.hyl.zhanmaoj.model.entity.TrueOrFalse;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class TrueOrFalseTestAdminVO implements Serializable {
    /**
     * id
     */
    private Long id;

    private Long questionId;

    /**
     * 标题
     */
    private String title;

    /**
     * 标签列表
     */
    private String tags;

    /**
     * 答案
     */
    private Integer answer;

    /**
     * 分数
     */
    private Integer score;

    private static final long serialVersionUID = 1L;
}
