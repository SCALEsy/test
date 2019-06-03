package com.example.test.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class myProxy implements InvocationHandler {

    private Object obj;


    public <T> T bind(Object o) {
        this.obj=o;
        T ob = (T) Proxy.newProxyInstance(this.obj.getClass().getClassLoader(), this.obj.getClass().getInterfaces(), this);
        return ob;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before");
        System.out.println(proxy.getClass().getName());
        Object result = method.invoke(obj, args);
        System.out.println("after");
        return result;
    }
}
