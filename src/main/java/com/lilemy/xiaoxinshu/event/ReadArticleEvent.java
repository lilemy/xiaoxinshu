package com.lilemy.xiaoxinshu.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 自定义文章被阅读事件
 *
 * @author lilemy
 * @date 2026-02-05 15:32
 */
@Getter
public class ReadArticleEvent extends ApplicationEvent {

    /**
     * 文章 ID
     */
    private Long articleId;

    public ReadArticleEvent(Object source, Long articleId) {
        super(source);
        this.articleId = articleId;
    }
}
