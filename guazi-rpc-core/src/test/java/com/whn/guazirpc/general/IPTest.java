package com.whn.guazirpc.general;

import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPTest {

    @Test
    public void getLocalIP() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            System.out.println("本机IP地址: " + localHost.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

}
