package cn.lilemy.xiaoxinshuinterface;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("cn.lilemy.xiaoxinshuinterface.mapper")
@SpringBootApplication
public class XiaoxinshuInterfaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiaoxinshuInterfaceApplication.class, args);
    }

}
