package cn.lilemy.xiaoxinshuinterface;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@MapperScan("cn.lilemy.xiaoxinshuinterface.mapper")
@SpringBootApplication
public class XiaoxinshuInterfaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiaoxinshuInterfaceApplication.class, args);
    }

}
