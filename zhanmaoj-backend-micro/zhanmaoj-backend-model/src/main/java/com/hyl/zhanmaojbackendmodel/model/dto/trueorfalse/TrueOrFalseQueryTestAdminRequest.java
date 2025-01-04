package com.hyl.zhanmaojbackendmodel.model.dto.trueorfalse;


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
public class TrueOrFalseQueryTestAdminRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long testId;



    private static final long serialVersionUID = 1L;
}