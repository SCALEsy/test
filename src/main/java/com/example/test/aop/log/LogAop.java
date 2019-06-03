package com.example.test.aop.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LogAop {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.example.test.rest.*.*(..))")
    public void point() {
    }

    @Around("point()")
    public Object logAndTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Object res = null;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        long time=0;
        try {
            logger.info("executor before path:" + request.getRequestURI());
            time = System.currentTimeMillis();
            res = joinPoint.proceed();
            time = System.currentTimeMillis() - time;
            logger.info("executor after path:" + request.getRequestURI() + ",cust time:" + time);
        } catch (Throwable throwable) {
            time = System.currentTimeMillis() - time;
            logger.info("executor error return by path:" + request.getRequestURI()+ ",cust time:" + time);
            throw throwable;
        }
        return res;
    }

    /*@AfterThrowing(pointcut = "point()", throwing = "throwable")
    public void error(Throwable throwable) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        logger.info("executor error return by path:" + request.getRequestURI());
    }*/
}
