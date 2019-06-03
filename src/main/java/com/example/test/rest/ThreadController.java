package com.example.test.rest;

import com.example.test.bean.result.Result;
import com.example.test.bean.result.ResultType;
import com.example.test.test.Accumulator;
import com.example.test.test.MyList;
import com.example.test.test.MyThreadPool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RestController
@Api("thread")
@Path("/thread")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ThreadController {
    @Resource
    private Accumulator accumulator;
    @Resource
    private MyThreadPool pool;
    @Resource
    private MyList list;

    @Path("/add")
    @ApiOperation("add")
    @GET
    public Result<Boolean> add() {
        pool.add(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                println(Thread.currentThread().getId() + ":" + i);
                accumulator.add();
            }
        });
        return new Result<>(ResultType.SUCCESS, true);
    }

    @Path("/get")
    @ApiOperation("get")
    @GET
    public Result<Integer> getValue() {

        return new Result<>(ResultType.SUCCESS, accumulator.get());
    }

    private void println(Object o) {
        System.out.println(o);
    }

    @Path("/addlist/{id}")
    @ApiOperation("addlist")
    @GET
    public Result<Boolean> addList(@PathParam("id") int id) {
        boolean res = list.add(id);
        return new Result<>(ResultType.SUCCESS, res);
    }
    @Path("/getlist")
    @ApiOperation("getlist")
    @GET
    public Result<List<Integer>> getList() {
        List<Integer> res = list.get();
        return new Result<>(ResultType.SUCCESS, res);
    }

}
