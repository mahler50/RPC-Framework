package com.whn.guazirpc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRegisterInfo<T> {

    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 实现类
     */
    private Class<? extends T> implClass;
}
