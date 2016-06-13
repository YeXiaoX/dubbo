package com.rmi.test;

import java.rmi.RemoteException;

/**
 * Created by Ivan on 2016/6/7.
 */
public class Hello extends java.rmi.server.UnicastRemoteObject implements HelloIn {
    public Hello() throws RemoteException {
        super();
    }

    public String sayHello(String name) throws RemoteException {
        return "Hello,World!"+name;
    }

    public static void main(String[] args) {
        System.setProperty("java.rmi.server.codebase","http://dubbo-register/com.rmi.test/");
        //System.setSecurityManager(new java.rmi.RMISecurityManager());
        try {

            Hello h = new Hello();
            HelloManImpl hm = new HelloManImpl();
            //开启RMI注册程序RMIRegistry
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            java.rmi.Naming.rebind("rmi://127.0.0.1:1099/hello", h);
            java.rmi.Naming.bind("rmi://127.0.0.1:1099/helloMan",hm);
            String ss[] = java.rmi.Naming.list("rmi://127.0.0.1:1099/");
            for(String s:ss){
                System.out.println("ss:"+s);
            }
            System.out.print("Ready......");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}