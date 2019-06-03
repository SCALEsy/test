package com.example.test.aop.httplimit;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
class HttplimitAop {
    @Value("${aop.http.limit}")
    private int limit_second;
    private Logger logger = LoggerFactory.getLogger(HttpLimit.class);
    @Autowired
    private StringRedisTemplate template;


    @Pointcut("within(com.example.test.rest.*)")
    public void mpoint() {

    }

    @Before(value = "mpoint() &&@annotation(limit)", argNames = "point,limit")
    public void limit(JoinPoint point, HttpLimit limit) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        int max = limit.num();
        String key = request.getRemoteAddr() + limit.path();
        Long v = template.opsForValue().increment(key, 1);
        template.expire(key, limit_second, TimeUnit.SECONDS);
        if (v > max) {
            logger.info("key: " + key + " submit " + v + " time in " + limit_second + " second");
            throw new HttpLimitException("key: " + key + " submit " + v + " time in " + limit_second + " second");
        }
    }

    private void println(Object o) {
        System.out.println(o);
    }
}
