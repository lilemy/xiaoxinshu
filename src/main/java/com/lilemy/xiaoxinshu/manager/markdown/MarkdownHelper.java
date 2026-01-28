package com.lilemy.xiaoxinshu.manager.markdown;

import com.lilemy.xiaoxinshu.manager.markdown.provider.NofollowLinkAttributeProvider;
import com.lilemy.xiaoxinshu.manager.markdown.renderer.ImageNodeRenderer;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.ext.image.attributes.ImageAttributesExtension;
import org.commonmark.ext.task.list.items.TaskListItemsExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Markdown 转换工具
 *
 * @author lilemy
 * @date 2025-12-26 14:56
 */
public class MarkdownHelper {

    // 正则表达式：匹配 Markdown 图片格式 ![alt](url) 和 HTML img 标签 src="url"
    private static final Pattern IMG_PATTERN = Pattern.compile("!\\[.*?\\]\\((.*?)\\)|<img.*?src=\"(.*?)\".*?>");

    /**
     * Markdown 解析器
     */
    private final static Parser PARSER;
    /**
     * HTML 渲染器
     */
    private final static HtmlRenderer HTML_RENDERER;

    // 初始化
    static {
        // Markdown 拓展
        List<Extension> extensions = Arrays.asList(
                // 表格拓展
                TablesExtension.create(),
                // 标题锚定项
                HeadingAnchorExtension.create(),
                // 图片宽高
                ImageAttributesExtension.create(),
                // 任务列表
                TaskListItemsExtension.create()
        );
        PARSER = Parser.builder().extensions(extensions).build();
        HTML_RENDERER = HtmlRenderer.builder()
                .attributeProviderFactory(context -> new NofollowLinkAttributeProvider())
                .nodeRendererFactory(ImageNodeRenderer::new)
                .extensions(extensions)
                .build();
    }


    /**
     * 将 Markdown 转换成 HTML
     *
     * @param markdown markdown 文本
     * @return HTML 文本
     */
    public static String convertMarkdown2Html(String markdown) {
        Node document = PARSER.parse(markdown);
        return HTML_RENDERER.render(document);
    }

    /**
     * 从 Markdown 文本中提取所有图片 URL
     *
     * @param content markdown 文本
     * @return 图片 URL 列表
     */
    public static List<String> extractImageUrls(String content) {
        List<String> urls = new ArrayList<>();
        if (content == null || content.isEmpty()) {
            return urls;
        }

        Matcher matcher = IMG_PATTERN.matcher(content);
        while (matcher.find()) {
            // group(1) 对应 MD 格式，group(2) 对应 HTML 格式
            String url = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
            if (url != null && !url.isEmpty()) {
                urls.add(url);
            }
        }
        return urls;
    }
}
