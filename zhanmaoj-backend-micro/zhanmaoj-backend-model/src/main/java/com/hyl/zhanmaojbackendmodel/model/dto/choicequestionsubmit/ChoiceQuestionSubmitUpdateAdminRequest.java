package com.hyl.zhanmaojbackendmodel.model.dto.choicequestionsubmit;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChoiceQuestionSubmitUpdateAdminRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 答案
     */
    private Integer answer;

    /**
     * 判题状态
     */
    private Integer status;


}
