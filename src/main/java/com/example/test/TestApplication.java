package com.example.test;

import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.context.ContextLoader;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import javax.sql.DataSource;
import java.util.Map;

@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan("com.example.test.dao")
public class TestApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(TestApplication.class, args);
        //System.out.println(context.getParentBeanFactory().getBean("testController").getClass().getName());
       /* String[] names = context.getBeanDefinitionNames();
        for (String str : names) {
            System.out.println(str);
        }*/
        HikariDataSource dataSource= (HikariDataSource) context.getBean("dataSource");

        //System.out.println(dataSource.getHikariPoolMXBean().getActiveConnections());

    }

}
