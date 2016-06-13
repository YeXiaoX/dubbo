package com.dubbo.provider;

import com.alibaba.dubbo.rpc.RpcContext;
import com.dubbo.demo.Demo2Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ivan on 2016/6/1.
 */
public class Demo2ServiceImpl implements Demo2Service {
    public String sayHi(String name){
        System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] Hi " + name + ", request from consumer: " + RpcContext.getContext().getRemoteAddress());
        return "Hi " + name + ", response form provider: " + RpcContext.getContext().getLocalAddress();
    }
}
