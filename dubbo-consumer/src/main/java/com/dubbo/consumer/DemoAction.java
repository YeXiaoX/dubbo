package com.dubbo.consumer;

import com.dubbo.demo.Demo2Service;
import com.dubbo.demo.DemoService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ivan on 2016/5/24.
 */
public class DemoAction {

    private DemoService demoService;

    private Demo2Service demo2Service;

    public void setDemoService(DemoService demoService) {
        this.demoService = demoService;
    }

    public void setDemo2Service(Demo2Service demo2Service){this.demo2Service = demo2Service;}

    public void start() throws Exception {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            try {
                String hello = demoService.sayHello("world" + i);
                System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] " + hello);
                String hi= demo2Service.sayHi(null);
                System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] " + i);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Thread.sleep(2000);
        }
    }

}
