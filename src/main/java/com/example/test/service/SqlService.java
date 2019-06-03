package com.example.test.service;

import java.util.List;

public interface SqlService {
    boolean select();
    boolean transfer();
    boolean transfer2();
    boolean test();
    void intest();
    boolean killInsert(List<String> list);
}
