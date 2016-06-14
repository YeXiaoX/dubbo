package com.dubbo.demo;

import java.io.Serializable;

public class Man implements Serializable {
    private String name=null;
    private Integer age=null;
    public Man() {
        System.out.println("none-arg constructor");
    }

    public Man(String name, Integer age) {
        System.out.println("arg constructor");
        this.name = name;
        this.age = age;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }



    @Override
    public String toString() {
        return "[" + name + ", " + age + "]";
    }
}
