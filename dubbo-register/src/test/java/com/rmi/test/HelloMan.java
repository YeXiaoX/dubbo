package com.rmi.test;

import java.rmi.RemoteException;

/**
 * Created by Ivan on 2016/6/8.
 */
public interface HelloMan extends java.rmi.Remote {
    String getName(Man man) throws RemoteException;
    int getAge(Man man) throws  RemoteException;
}
