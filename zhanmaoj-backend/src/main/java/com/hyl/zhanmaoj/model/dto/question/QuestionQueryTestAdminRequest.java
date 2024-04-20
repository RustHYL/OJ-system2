package com.hyl.zhanmaoj.model.dto.question;


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
public class QuestionQueryTestAdminRequest extends PageRequest implements Serializable {


    private Long testId;



    private static final long serialVersionUID = 1L;
}