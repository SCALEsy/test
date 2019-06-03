package com.example.test.rest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.test.bean.result.Result;
import com.example.test.bean.result.ResultType;
import com.example.test.service.SqlService;
import com.example.test.service.TestService;
import com.example.test.test.MyThreadPool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@Api(tags = "Test")
//@RequestMapping(path = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Produces({MediaType.APPLICATION_JSON + ";charset=UTF-8", MediaType.APPLICATION_XML})
@Path("/")
public class TestController {
    @Autowired
    private StringRedisTemplate template;
    @Autowired
    private MyThreadPool excutor;
    @Autowired
    private SqlService sqlService;
    @Autowired
    private TestService testService;


    //@RequestMapping(path = "/test",method = RequestMethod.GET)
    @GET
    @Path("/test")
    @ApiOperation("测试")
    public Result<Map<String, String>> test() {
        template.opsForValue().set("test", "sy", 10L, TimeUnit.SECONDS);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("test", "asd");

        Long time = template.getExpire("test");
        println(time);

        return new Result<>(ResultType.SUCCESS, map);
    }

    private void println(Object o) {
        System.out.println(o);
    }


    @GET
    @Path("/login")
    @ApiOperation("get token")
    public Result<String> login() {
        long now = System.currentTimeMillis();
        long time = now + 3600 * 1000;
        String token = JWT.create().withClaim("user", "test")
                .withClaim("date", now)
                .withIssuedAt(new Date(now))
                .withIssuer("sy")
                .withNotBefore(new Date(now))
                .withExpiresAt(new Date(time))
                .sign(Algorithm.HMAC256("test"));
        return new Result<>(ResultType.SUCCESS, token);
    }


    //@RequestMapping(path = "/check",method = RequestMethod.POST)
    @ApiOperation("测试")
    @GET
    @Path("/check")
    public Result<Boolean> checkToken(@RequestParam(required = true, value = "token") @QueryParam("token") String token) {
        JWTVerifier veritier = JWT.require(Algorithm.HMAC256("test"))
                .withIssuer("sy")
                .build();
        DecodedJWT jwt = veritier.verify(token);
        println(jwt.getClaim("user").asString());
        println(String.valueOf(Base64.getUrlDecoder().decode(jwt.getHeader())));
        return new Result<>(ResultType.SUCCESS, true);
    }


    @ApiOperation("token")
    //@RequestMapping(path = "/test2",method = RequestMethod.GET)
    @GET
    @Path("/test2")
    public Result<Boolean> test2(/*@QueryParam("test") @ApiParam(required = true, value = "as") String test,
                                @FormParam("test2") @ApiParam(required = true, value = "as2") String test2
*/
    ) {
        //sqlService.select();
        boolean res = sqlService.test();
        return new Result<>(ResultType.SUCCESS, res);
    }

    @ApiOperation("token")
    // @RequestMapping(path = "/test3",method = RequestMethod.GET)
    @GET
    @Path("/test3")
    public Result<Boolean> test3() {
        boolean res = false;
        try {
            res = sqlService.transfer2();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result<>(ResultType.SUCCESS, res);
    }


    @ApiOperation("token")
    //@RequestMapping(path = "/addthread",method = RequestMethod.GET)
    @GET
    @Path("/addthread")
    public Result<Boolean> add(@ApiParam(required = true, value = "as") @QueryParam("test") String test) {
        this.excutor.add(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    println(test + i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return new Result<>(ResultType.SUCCESS, true);
    }
    @GET
    @ApiOperation("/index")
    @Path("/index")
    @Produces({MediaType.TEXT_HTML})
    public String index() throws IOException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("/META-INF/resource.webjars.swagger-ui.2.2.10/index.html");
        println(this.getClass().getClassLoader().getResource("").getPath());
        byte[] bytes=new byte[1024];
        StringBuilder builder=new StringBuilder();
        int i=0;
        while ((i=in.read(bytes))!=-1){
            builder.append(bytes);
        }
        return builder.toString();
    }
}
