package com.example.test.test;

import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class Accumulator {
    private int count = 0;
    private ReentrantLock lock;
    private Condition condition = null;

    public Accumulator() {
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }

    public void add() {

        try {
            lock.lock();

            count += 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

    public int get() {
        return count;
    }
}
