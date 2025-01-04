package com.hyl.zhanmaojbackendmodel.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 题目提交编程语言枚举
 *
 */
public enum SMSStatusEnum {

    MODIFY_PHONE("修改手机", "MODIFYPHONE"),
    MODIFY_PASSWORD("修改密码", "MODIFYPASSWORD"),
    BIND_PHONE("绑定手机", "BINDPHONE"),
    FIND_PASSWORD("找回密码", "FINDPASSWORD");

    private final String text;

    private final String value;

    SMSStatusEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static SMSStatusEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (SMSStatusEnum anEnum : SMSStatusEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

}
