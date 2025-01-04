package com.hyl.zhanmaojbackendmodel.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色枚举
 *
 */
public enum ChoiceQuestionAnswerEnum {

    OPTION_NULL("未填写", 0),
    OPTION_A("选项A", 1),
    OPTION_B("选项B", 2),
    OPTION_C("选项C", 3),
    OPTION_D("选项D", 4);

    private final String text;

    private final Integer value;

    ChoiceQuestionAnswerEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static ChoiceQuestionAnswerEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (ChoiceQuestionAnswerEnum anEnum : ChoiceQuestionAnswerEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
