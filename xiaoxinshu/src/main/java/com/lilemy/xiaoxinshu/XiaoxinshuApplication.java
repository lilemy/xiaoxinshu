package com.lilemy.xiaoxinshu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
public class XiaoxinshuApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiaoxinshuApplication.class, args);
    }

}
