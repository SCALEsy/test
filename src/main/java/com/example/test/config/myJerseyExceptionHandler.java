package com.example.test.config;

import com.example.test.bean.result.Result;
import com.example.test.bean.result.ResultType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

@Component
public class myJerseyExceptionHandler implements ExceptionMapper<Exception> {
    @Value("${exception.handler.printstacktrace}")
    private boolean ishow;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Response toResponse(Exception e) {
        if (ishow) {
            e.printStackTrace();
        }
        logger.info("request error catched by myJerseyExceptionHandler case by:" + e.getMessage());
        Result<String> result = new Result(ResultType.FAILURE, e.getMessage());
        Response.ResponseBuilder builder = Response.ok(result, MediaType.APPLICATION_JSON);
        return builder.build();
    }


}
