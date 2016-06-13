package com.rmi.test;

import java.rmi.RemoteException;

/**
 * Created by Ivan on 2016/6/8.
 */
public class HelloManImpl extends java.rmi.server.UnicastRemoteObject implements HelloMan {

    public int getAge(Man man) throws RemoteException {
        return man.getAge();
    }

    public HelloManImpl() throws RemoteException {
        super();
    }

    public String getName(Man man) throws RemoteException {
        return man.getName();
    }
}
