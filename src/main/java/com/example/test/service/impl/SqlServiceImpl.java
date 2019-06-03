package com.example.test.service.impl;

import com.example.test.service.SqlService;
import io.swagger.models.auth.In;
import org.springframework.aop.framework.AopContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SqlServiceImpl implements SqlService {
    @Resource
    private JdbcTemplate template;
    /*@Resource
    private DataSource dataSource;*/

    @Override
    public boolean select() {
        String sql = "select * from test limit 0,5";

        List<Map<String, Object>> list = template.queryForList(sql);
        int i = 0;
        for (Map<String, Object> map : list) {
            i += 1;
            println(i + "------------");
            for (Map.Entry entry : map.entrySet()) {
                println(entry.getKey() + "\t" + entry.getValue());
            }
        }
        println(template.getQueryTimeout());
        return false;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public boolean transfer() {
        String sql = "select money from test where id=?";
        double start = template.queryForObject(sql, Double.class, 1);
        double end = template.queryForObject(sql, Double.class, 2);

        println(start + "  " + end);
        if (start - 100 > 0) {
            start = start - 100;
            end += 100;
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String update = "update test set money=? where id=?";
        template.update(update, start, 1);
        template.update(update, end, 2);
        return true;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public boolean transfer2() {
        String sql = "select money from test where id=?";
        double start = template.queryForObject(sql, Double.class, 2);
        double end = template.queryForObject(sql, Double.class, 1);
        println(start + "  " + end);
        if (start - 100 > 0) {
            start = start - 100;
            end += 100;
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String update = "update test set money=? where id=?";
        template.update(update, start, 2);
        template.update(update, end, 1);
        return true;
    }

    @Override
    @Transactional
    public boolean test() {
        String sql="update test set money=20 where id=4";
        int res=template.update(sql);

        ((SqlService)AopContext.currentProxy()).intest();
        String sql2="update test set money=11 where id=5";
        res=template.update(sql2);
        if(res>0){
            throw new RuntimeException("just error");
        }
        return res>0;
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void intest(){
        String sql="update test set money=15 where id=3";
        int res = template.update(sql);

    }

    @Override
    public boolean killInsert(List<String> list) {
        if(list==null||list.size()<=0){
            throw new RuntimeException("insert list is empty");
        }
        List<Object[]> ids=new ArrayList<>(list.size());
        for(String s:list){
            Integer[] arr=new Integer[1];
            arr[0]=Integer.valueOf(s);
            ids.add(arr);
        }

        String sql="insert into `kill`(`id`) values(?)";
        template.batchUpdate(sql,ids);
        return true;
    }
    private void println(Object o) {
        System.out.println(o);
    }


}
