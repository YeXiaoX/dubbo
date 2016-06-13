package com.rmi.test;

import java.io.Serializable;

/**
 * Created by Ivan on 2016/6/8.
 */
public class Man implements Serializable{
    private static final long serialVersionUID = 259668523926438264L;
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
