package cn.lilemy.xiaoxinshu.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 检测爬虫
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CrawlerDetection {

    /**
     * 调用多少次警告
     *
     * @return 调用多少次警告
     */
    int warnCount() default 20;

    /**
     * 调用多少次封号
     *
     * @return 调用多少次封号
     */
    int banCount() default 40;

    /**
     * 时间间隔
     *
     * @return 时间间隔
     */
    int timeInterval() default 1;

    /**
     * 时间间隔单位
     *
     * @return 时间间隔单位
     */
    TimeUnit timeUnit() default TimeUnit.MINUTES;

    /**
     * 计数器缓存过期时间（秒）
     *
     * @return 计数器缓存过期时间（秒）
     */
    int expirationTimeInSeconds() default 180;
}
