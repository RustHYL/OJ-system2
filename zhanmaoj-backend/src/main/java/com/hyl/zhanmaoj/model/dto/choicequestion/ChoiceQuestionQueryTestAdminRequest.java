package com.hyl.zhanmaoj.model.dto.choicequestion;


import com.hyl.zhanmaoj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询请求
 *

 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChoiceQuestionQueryTestAdminRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long testId;


    private static final long serialVersionUID = 1L;
}