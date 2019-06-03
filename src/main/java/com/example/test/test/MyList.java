package com.example.test.test;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class MyList {
    private int max;
    private List<Integer> list;
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();


    public MyList() {
        /*max = 10;
        list=new ArrayList<>(max);*/
    }

    @PostConstruct
    public void init() {
        this.max = 10;
        max = 10;
        list = new ArrayList<>(max);
    }

    public Boolean add(int id) {
        boolean flag = false;
        if (list.size() >= max) {
            return false;
        }
        try {
            lock.lock();
            if (list.size() < max) {
                this.list.add(id);
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return flag;
    }

    public List<Integer> get() {
        return this.list;

    }

}
