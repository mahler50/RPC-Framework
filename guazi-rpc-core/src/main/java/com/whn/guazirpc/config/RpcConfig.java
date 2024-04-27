package com.whn.guazirpc.config;

import lombok.Data;

/**
 * RPC 框架配置
 */
@Data
public class RpcConfig {

    private RpcConfig() {
    }

    /**
     * 名称
     */
    private String name = "guazi-rpc";

    /**
     * 版本
     */
    private String version = "1.0";

    /**
     * 服务器主机名
     */
    private String serverHost = "localhost";

    /**
     * 服务器端口号
     */
    private Integer serverPort = 8080;
}