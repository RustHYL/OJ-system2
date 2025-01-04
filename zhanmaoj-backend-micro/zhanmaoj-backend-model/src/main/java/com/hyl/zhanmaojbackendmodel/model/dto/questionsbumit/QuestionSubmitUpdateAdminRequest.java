package com.hyl.zhanmaojbackendmodel.model.dto.questionsbumit;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionSubmitUpdateAdminRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 判题信息（json 对象）
     */
    private String judgeInfo;

    /**
     * 判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）
     */
    private Integer status;


}
