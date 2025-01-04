package com.hyl.zhanmaojbackendmodel.model.vo;


import com.hyl.zhanmaojbackendcommon.common.PageRequest;
import com.hyl.zhanmaojbackendmodel.model.entity.TrueOrFalseSubmit;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

@Data
public class TrueOrFalseSubmitVO extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 答案 0 正确 1错误
     */
    private Integer answer;

    /**
     * 判题状态（0 - 错误、1 - 正确）
     */
    private Integer status;

    /**
     * 题目 id
     */
    private Long trueOrFalseId;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 试卷 id
     */
    private Long testId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 提交用户信息
     */
    private UserVO userVO;

    /**
     * 对应题目信息
     */
    private TrueOrFalseVO trueOrFalseVO;

    /**
     * 包装类转对象
     *
     * @param trueOrFalseSubmitVO
     * @return
     */
    public static TrueOrFalseSubmit voToObj(TrueOrFalseSubmitVO trueOrFalseSubmitVO) {
        if (trueOrFalseSubmitVO == null) {
            return null;
        }
        TrueOrFalseSubmit trueOrFalseSubmit = new TrueOrFalseSubmit();
        BeanUtils.copyProperties(trueOrFalseSubmitVO, trueOrFalseSubmit);
        return trueOrFalseSubmit;
    }

    /**
     * 对象转包装类
     *
     * @param trueOrFalseSubmit
     * @return
     */
    public static TrueOrFalseSubmitVO objToVo(TrueOrFalseSubmit trueOrFalseSubmit) {
        if (trueOrFalseSubmit == null) {
            return null;
        }
        TrueOrFalseSubmitVO trueOrFalseSubmitVO = new TrueOrFalseSubmitVO();
        BeanUtils.copyProperties(trueOrFalseSubmit, trueOrFalseSubmitVO);
        return trueOrFalseSubmitVO;
    }

    private static final long serialVersionUID = 1L;
}
