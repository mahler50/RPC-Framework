package com.whn.guazirpc.bootstrap;

import com.whn.guazirpc.RpcApplication;

/**
 * 服务消费者启动类
 */
public class ConsumerBootstrap {

    /**
     * 初始化
     */
    public static void init() {
        // Rpc 服务初始化
        RpcApplication.init();
    }
}
