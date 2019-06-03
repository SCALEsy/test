package com.example.test.test;

import org.springframework.stereotype.Component;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
@Component
public class MyThreadPool {
    private LinkedBlockingDeque<Runnable> myque = new LinkedBlockingDeque<>();
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 5, 60, TimeUnit.SECONDS, myque);


    public void add(Runnable runnable) {
        this.executor.execute(runnable);
    }


}
