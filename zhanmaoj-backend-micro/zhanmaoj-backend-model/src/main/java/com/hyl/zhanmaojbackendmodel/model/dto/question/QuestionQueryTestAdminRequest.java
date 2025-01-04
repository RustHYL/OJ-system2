package com.hyl.zhanmaojbackendmodel.model.dto.question;


import com.hyl.zhanmaojbackendcommon.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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