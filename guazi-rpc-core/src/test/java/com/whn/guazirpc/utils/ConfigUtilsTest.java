package com.whn.guazirpc.utils;

import com.whn.guazirpc.config.RpcConfig;
import com.whn.guazirpc.constant.RpcConstant;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.*;

@Slf4j
public class ConfigUtilsTest extends TestCase {

    public void testLoadConfig() {
        RpcConfig rpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        log.info("rpcConfig:{}", rpcConfig);
    }
}