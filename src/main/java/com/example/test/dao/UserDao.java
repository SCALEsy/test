package com.example.test.dao;

import com.example.test.bean.Test;
import com.example.test.bean.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface UserDao {
    @Select("select * from test")
    List<Test> selectAll();

    @Select("select * from user where id=#{id}")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "create_time", column = "create_time"),
            @Result(property = "update_time", column = "update_time"),
            @Result(property = "list", column = "id", //javaType = List.class,
                    many = @Many(select = "com.example.test.dao.TestDao.selectTestByUserId",fetchType = FetchType.EAGER))

    })
    List<User> users(Integer id);


}
