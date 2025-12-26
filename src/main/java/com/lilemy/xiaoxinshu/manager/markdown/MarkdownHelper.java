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

import java.util.Arrays;
import java.util.List;

/**
 * Markdown 转换工具
 *
 * @author lilemy
 * @date 2025-12-26 14:56
 */
public class MarkdownHelper {

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
}
