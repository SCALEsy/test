package com.example.test.dao;

import com.example.test.bean.Test;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface TestDao {
    @Select("select * from test where user_id=#{user_id}")
    /*@Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "age", column = "age"),
            @Result(property = "sex", column = "sex"),
            @Result(property = "money", column = "money"),
            @Result(property = "user_id", column = "user_id", one = @One(select = "com.example.test.dao.users"))
    })*/
    List<Test> selectTestByUserId(Integer user_id);
}
