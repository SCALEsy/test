package com.example.test.config;


import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.fasterxml.jackson.jaxrs.xml.JacksonXMLProvider;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jersey.JerseyAutoConfiguration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class JerseyConfig extends ResourceConfig {
    @Value("${server.servlet.path}")
    private String APPLICATION_PATH;

    @Autowired
    private myJerseyExceptionHandler handler;

    private static final String RESOURCE_PACKAGE_NAME = "com.example.test.rest";
    //private static final String AOP_PACKAGE = "com.example.test.aop";
    /**
     * 覆盖jersey logging 自带的jul logger
     */
    private static final Logger LOGGER = Logger.getLogger("jersey-logger");

    public JerseyConfig() {
        // 移除根日志处理器
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        // 绑定新的处理器
        SLF4JBridgeHandler.install();
        // 请求 响应日志
        LOGGER.setLevel(Level.FINE);
        LoggingFeature lf = new LoggingFeature(LOGGER);
        LOGGER.info("jersey config ");
        this.register(lf);
        // 配置Swagger
        this.register(ApiListingResource.class);
        this.register(SwaggerSerializers.class);
        //this.register(myJerseyExceptionHandler.class);
        this.register(JacksonJsonProvider.class);
        this.register(JacksonXMLProvider.class);

        packages(RESOURCE_PACKAGE_NAME);


    }

    @PostConstruct
    public void initSwagger() {
        BeanConfig config = new BeanConfig();
        config.setTitle("TEST");
        config.setDescription("my microservice seed");
        config.setVersion("1.0.0");
        config.setContact("sy");
        config.setSchemes(new String[]{"http", "https"});
        config.setBasePath(this.APPLICATION_PATH);
        config.setResourcePackage(JerseyConfig.RESOURCE_PACKAGE_NAME);
        config.setPrettyPrint(true);
        config.setScan(true);
        //注册exceptionhandler
        this.register(handler);

       /* Set<Object> set = this.getInstances();
        for (Object o : set) {
            System.out.println(o.getClass().getName());
        }
        for (Class c : this.getClasses()) {
            System.out.println(c.getName());
        }*/


    }

}
