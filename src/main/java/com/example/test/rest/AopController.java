package com.example.test.rest;

import com.example.test.aop.httplimit.HttpLimit;
import com.example.test.bean.User;
import com.example.test.bean.result.Result;
import com.example.test.bean.result.ResultType;
import com.example.test.bean.Test;
import com.example.test.dao.TestDao;
import com.example.test.dao.UserDao;
import com.example.test.service.SqlService;
import com.example.test.test.MyThreadPool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ContextLoader;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;


@RestController
@Api(tags = "AOP")
//@RequestMapping(path = "/aop", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Path("/aop")
@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
public class AopController {
    @Autowired
    private StringRedisTemplate template;
    @Autowired
    private MyThreadPool pool;
    @Autowired
    private SqlService sqlService;
    @Autowired
    private UserDao dao;

    @ApiOperation("aop test")
    //@HttpLimit(num = 3, path = "/test/id")
    @GET
    @Path("/test/{id}")
    public Result<Boolean> test1(@QueryParam("a") @ApiParam(required = true, defaultValue = "test") String a,
                                 @PathParam("id") @ApiParam(required = true, defaultValue = "0") int id
    ) {
        /*Long size = template.opsForList().size("list");
        if (size < 10) {
            template.opsForList().rightPush("list", id + "");
        }*/
        boolean res = template.execute(new SessionCallback<Boolean>() {
            @Override
            public <K, V> Boolean execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
                StringRedisTemplate t = (StringRedisTemplate) redisOperations;
                List<Object> list = null;
                t.watch("num");
                String str = t.opsForValue().get("num");
                int num = str == null ? 0 : Integer.valueOf(str);
                if (num < 10) {
                    t.multi();
                    t.opsForValue().increment("num", 1L);
                    t.opsForList().rightPush("list", id + "");
                    list = t.exec();
                }
                return num < 10 && list.size() > 0;
            }
        });

        return new Result<>(ResultType.SUCCESS, res);
    }

    @GET
    @ApiOperation("list")
    @Path("/list")
    public Result<List<String>> getList() {
        List<String> list = template.opsForList().range("list", 0, -1);
        template.delete("num");
        template.delete("list");
        println(list.size());
        /*this.pool.add(new Runnable() {
            @Override
            public void run() {
                System.out.println("thread start");
                sqlService.killInsert(list);
                System.out.println("thread stop");
            }
        });*/

        return new Result<>(ResultType.SUCCESS, list);
    }

    @GET
    @ApiOperation("list2")
    @HttpLimit(path = "/list2", num = 3)
    @Path("/list2")
    public Result<Integer> getList2() {
        int res = template.execute(new SessionCallback<Integer>() {
            @Override
            public <K, V> Integer execute(RedisOperations<K, V> redisOperations) throws DataAccessException {

                StringRedisTemplate t = (StringRedisTemplate) redisOperations;
                List<Object> rs = null;
                int v = 0;
                int i = 0;
                do {
                    t.watch("sy");
                    String rv = t.opsForValue().get("sy");
                    println(rv);
                    v = Integer.valueOf(rv == null ? "0" : rv);
                    v = v + 1;
                    t.multi();
                    t.opsForValue().set("sy", v + "");
                    try {
                        rs = t.exec();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    i++;
                    println("i:" + i);
                    println("rs:" + rs);
                } while (rs == null || rs.size() <= 0);


                return v;
            }
        });
        return new Result<>(ResultType.SUCCESS, res);
    }

    private void println(Object o) {
        System.out.println(o);
    }

    @GET
    @ApiOperation("mapper")
    @Path("/mapper")
    public Result<List<Test>> mapper() {
        List<Test> list = dao.selectAll();
        return new Result<>(ResultType.SUCCESS, list);
    }

    @GET
    @ApiOperation("/redis")
    @Path("/redis")
    public Result<Integer> getRedis() {
        //template.getConnectionFactory().getConnection();
        template.opsForValue().increment("sy", 1L);
        List<RedisClientInfo> list = this.template.getClientList();
        for (RedisClientInfo info : list) {
            println(info.getName() + info.getAddressPort() + info.getAge());
        }
        return new Result<>(ResultType.SUCCESS, list.size());
    }

    @GET
    @ApiOperation("mapper2")
    @Path("/mapper2")
    public Result<List<User>> mapper2() {
        List<User> list = dao.users(1);
        return new Result<>(ResultType.SUCCESS, list);
    }
}
