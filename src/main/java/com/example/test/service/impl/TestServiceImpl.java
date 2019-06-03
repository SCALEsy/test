package com.example.test.service.impl;

import com.example.test.service.SqlService;
import com.example.test.service.TestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service
public class TestServiceImpl implements TestService {
    @Resource
    private SqlService sqlService;

    @Override
    public Boolean test() {
        return null;
    }
    /*@Resource
    private JdbcTemplate template;

    @Override
    public Boolean test() {
        System.out.println("asd");
        String sql="update test set money=20 where id=4";
        int res=template.update(sql);
        sqlService.intest();
        String sql2="update test set money=11 where id=5";
        res=template.update(sql2);
       *//* if(res>0){
            throw new RuntimeException("just error");
        }*//*
        return true;
    }*/

}
