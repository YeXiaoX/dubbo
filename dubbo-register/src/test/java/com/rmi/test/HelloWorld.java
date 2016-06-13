package com.rmi.test;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.rmi.Naming;

/**
 * Created by Ivan on 2016/6/7.
 */
public class HelloWorld {
    public static void main(String[] args){
        try{
            HelloIn hi=(HelloIn) Naming.lookup("rmi://localhost:1099/hello");
            HelloMan hm=(HelloMan) Naming.lookup("rmi://127.0.0.1:1099/helloMan");
            Man man = new Man();
            man.setAge(10);
            man.setName("huahua");
            System.out.println(System.currentTimeMillis());
           System.out.println("name:"+ hm.getName(man));
            System.out.println(System.currentTimeMillis());
            for(int i=0;i<10;i++){
                System.out.println(hi.sayHello(i+"次！"));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}