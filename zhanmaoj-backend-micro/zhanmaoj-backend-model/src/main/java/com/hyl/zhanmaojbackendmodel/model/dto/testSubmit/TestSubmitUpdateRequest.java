package com.hyl.zhanmaojbackendmodel.model.dto.testSubmit;

import lombok.Data;

import java.io.Serializable;

@Data
public class TestSubmitUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 得分
     */
    private Integer score;


}
