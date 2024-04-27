package com.whn.example.consumer;

import com.whn.guazirpc.config.RpcConfig;
import com.whn.guazirpc.utils.ConfigUtils;

public class ConsumerExample {
    public static void main(String[] args) {
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpc);
    }
}
