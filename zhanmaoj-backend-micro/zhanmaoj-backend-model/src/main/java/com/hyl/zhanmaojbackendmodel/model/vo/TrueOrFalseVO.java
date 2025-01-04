package com.hyl.zhanmaojbackendmodel.model.vo;

import cn.hutool.json.JSONUtil;
import com.hyl.zhanmaojbackendmodel.model.entity.TrueOrFalse;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class TrueOrFalseVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 内容
     */
    private String title;

    /**
     * 题目提交数
     */
    private Integer submitNum;

    /**
     * 题目通过数
     */
    private Integer acceptedNum;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人信息
     */
    private UserVO userVO;

    /**
     * 包装类转对象
     *
     * @param trueOrFalseVO
     * @return
     */
    public static TrueOrFalse voToObj(TrueOrFalseVO trueOrFalseVO) {
        if (trueOrFalseVO == null) {
            return null;
        }
        TrueOrFalse trueOrFalse = new TrueOrFalse();
        BeanUtils.copyProperties(trueOrFalseVO, trueOrFalse);
        List<String> tagList = trueOrFalseVO.getTags();
        if (tagList != null) {
            trueOrFalse.setTags(JSONUtil.toJsonStr(tagList));
        }
        return trueOrFalse;
    }

    /**
     * 对象转包装类
     *
     * @param trueOrFalse
     * @return
     */
    public static TrueOrFalseVO objToVo(TrueOrFalse trueOrFalse) {
        if (trueOrFalse == null) {
            return null;
        }
        TrueOrFalseVO trueOrFalseVO = new TrueOrFalseVO();
        BeanUtils.copyProperties(trueOrFalse, trueOrFalseVO);
        List<String> tagList = JSONUtil.toList(trueOrFalse.getTags(), String.class);
        trueOrFalseVO.setTags(tagList);
        return trueOrFalseVO;
    }

    private static final long serialVersionUID = 1L;
}
