package com.whn.guazirpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

/**
 * 协议消息结构
 * @param <T>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtocolMessage<T> {

    /**
     * 消息头
     */
    private Header header;

    /**
     * 消息体
     */
    private T body;

    /**
     * 协议消息头
     */
    @Data
    public static class Header {
        /**
         * 魔数
         */
        private byte magic;

        /**
         * 版本号
         */
        private byte version;

        /**
         * 序列化器
         */
        private byte serializer;

        /**
         * 类型（请求/响应）
         */
        private byte type;

        /**
         * 状态码
         */
        private byte status;

        /**
         * 请求Id
         */
        private long requestId;

        /**
         * 消息体长度
         */
        private int bodyLength;
    }
}
