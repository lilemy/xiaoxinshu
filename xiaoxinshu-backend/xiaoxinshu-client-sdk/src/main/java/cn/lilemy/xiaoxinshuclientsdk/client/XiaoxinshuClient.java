package cn.lilemy.xiaoxinshuclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.lilemy.xiaoxinshuclientsdk.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 调用第三方接口的客户端
 */
@Slf4j
public class XiaoxinshuClient {
    private static final String GATEWAY_HOST = "http://localhost:8100";

    private final String accessKey;

    private final String secretKey;

    public String getRandomImage() {
        HttpResponse httpResponse = HttpRequest.get(GATEWAY_HOST + "/api/image/random")
                .addHeaders(getHeaderMap())
                .execute();
        log.info(String.valueOf(httpResponse.getStatus()));
        return httpResponse.body();
    }

    public String getRandomImage(String type) {
        HttpResponse httpResponse = HttpRequest.get(GATEWAY_HOST + "/api/image/random?type=" + type)
                .addHeaders(getHeaderMap())
                .execute();
        log.info(String.valueOf(httpResponse.getStatus()));
        return httpResponse.body();
    }

    public XiaoxinshuClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    private Map<String, String> getHeaderMap(String body) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        hashMap.put("body", body);
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", SignUtils.getSign(body, secretKey));
        return hashMap;
    }

    private Map<String, String> getHeaderMap() {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", SignUtils.getSign(accessKey, secretKey));
        return hashMap;
    }

}
