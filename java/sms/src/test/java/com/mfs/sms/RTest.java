package com.mfs.sms;


public class RTest {
    public static void main(String[] args) {
        new S();
    }
}

class T {
    static {
        System.out.println("T 静态码块");
    }
    {
        System.out.println("T 代码块");
    }
   public T() {
        System.out.println("T 构造方法");
    }
}

class S extends T {
    static {
        System.out.println("S 静态代码块");
    }
    {
        System.out.println("S 代码块");
    }
    public S() {
        System.out.println("S 构造方法");
    }
}