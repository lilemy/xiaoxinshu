package com.lilemy.xiaoxinshu.event.subscriber;

import com.lilemy.xiaoxinshu.event.ReadArticleEvent;
import com.lilemy.xiaoxinshu.service.ArtArticleService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 阅读文章事件监听
 *
 * @author lilemy
 * @date 2026-02-05 15:34
 */
@Slf4j
@Component
public class ReadArticleSubscriber implements ApplicationListener<ReadArticleEvent> {

    @Resource
    private ArtArticleService artArticleService;

    @Override
    @Async("threadPoolTaskExecutor")
    public void onApplicationEvent(ReadArticleEvent event) {
        Long articleId = event.getArticleId();
        log.info("==> 文章阅读事件消费成功，articleId: {}", articleId);
        artArticleService.increaseReadNum(articleId);
    }
}
