package com.whn.guazirpc.protocol;

import lombok.Getter;

/**
 * 协议消息状态枚举类
 */
@Getter
public enum ProtocolStatusEnum {

    OK("ok", 20),
    BAD_REQUEST("badRequest", 40),
    BAD_RESPONSE("badResponse", 50);

    private final String text;

    private final int value;

    ProtocolStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举值
     * @param value
     * @return
     */
    public static ProtocolStatusEnum getEnumByValue(int value) {
        for (ProtocolStatusEnum anEnum :
                ProtocolStatusEnum.values()) {
            if (anEnum.value == value) {
                return anEnum;
                }
            }
        return null;
    }
}
