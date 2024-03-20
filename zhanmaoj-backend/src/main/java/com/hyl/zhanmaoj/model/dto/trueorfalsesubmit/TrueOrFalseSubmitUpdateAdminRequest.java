package com.hyl.zhanmaoj.model.dto.trueorfalsesubmit;

import lombok.Data;

import java.io.Serializable;

@Data
public class TrueOrFalseSubmitUpdateAdminRequest implements Serializable {

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
