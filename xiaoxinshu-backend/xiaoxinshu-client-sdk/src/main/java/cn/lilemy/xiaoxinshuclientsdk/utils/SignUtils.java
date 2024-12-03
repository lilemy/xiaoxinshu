package cn.lilemy.xiaoxinshuclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * 签名工具
 */
public class SignUtils {

    /**
     * 生成签名
     *
     * @param body      参数
     * @param secretKey 签名
     * @return 加密签名
     */
    public static String getSign(String body, String secretKey) {
        Digester digester = new Digester(DigestAlgorithm.SHA256);
        String content = body + "." + secretKey;
        return digester.digestHex(content);
    }
}
