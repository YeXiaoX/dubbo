package com.rmi.test;

import java.rmi.RemoteException;

public interface HelloIn extends java.rmi.Remote{
    String sayHello(String name) throws RemoteException;
}