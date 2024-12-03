package cn.lilemy.xiaoxinshu.client;

import cn.lilemy.xiaoxinshuclientsdk.client.XiaoxinshuClient;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class XiaoxinshuInterfaceTest {

    @Resource
    private XiaoxinshuClient xiaoxinshuClient;

    @Test
    void test() {
        String image = xiaoxinshuClient.getRandomImage();
        log.info(image);
    }
}
