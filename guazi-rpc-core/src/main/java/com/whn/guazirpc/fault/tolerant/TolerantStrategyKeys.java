package com.whn.guazirpc.fault.tolerant;

/**
 * 容错策略键值
 */
public interface TolerantStrategyKeys {

    /**
     * 快速失败
     */
    String FAIL_FAST = "failFast";

    /**
     * 静默处理
     */
    String FAIL_SAFE = "failSafe";
}
