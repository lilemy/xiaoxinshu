package com.lilemy.xiaoxinshu.manager.markdown.provider;

import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.renderer.html.AttributeProvider;

import java.util.Map;

/**
 * 自定义 AttributeProvider - 超链接中动态添加 nofollow
 *
 * @author lilemy
 * @date 2025-12-26 15:27
 */
public class NofollowLinkAttributeProvider implements AttributeProvider {

    /**
     * 网站域名
     */
    private final static String DOMAIN = "lilemy.cn";

    /**
     * 设置属性
     *
     * @param node       节点
     * @param tagName    标签名
     * @param attributes 属性
     */
    @Override
    public void setAttributes(Node node, String tagName, Map<String, String> attributes) {
        if (node instanceof Link linkNode) {
            // 获取链接地址
            String href = linkNode.getDestination();
            // 如果链接不是自己域名，则添加 rel="nofollow" 属性
            if (!href.contains(DOMAIN)) {
                attributes.put("rel", "nofollow");
            }
        }
    }
}
