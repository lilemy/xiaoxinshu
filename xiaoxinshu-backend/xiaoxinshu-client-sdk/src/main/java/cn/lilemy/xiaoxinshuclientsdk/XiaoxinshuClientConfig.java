package cn.lilemy.xiaoxinshuclientsdk;

import cn.lilemy.xiaoxinshuclientsdk.client.XiaoxinshuClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Data
@ComponentScan
@Configuration
@ConfigurationProperties("xiaoxinshu.client")
public class XiaoxinshuClientConfig {

    private String accessKey;

    private String secretKey;

    @Bean
    public XiaoxinshuClient xiaoxinshuClient() {
        return new XiaoxinshuClient(accessKey, secretKey);
    }
}
