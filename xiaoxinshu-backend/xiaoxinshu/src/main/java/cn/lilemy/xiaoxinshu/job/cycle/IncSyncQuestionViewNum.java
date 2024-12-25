package cn.lilemy.xiaoxinshu.job.cycle;

import cn.hutool.core.collection.CollUtil;
import cn.lilemy.xiaoxinshu.constant.RedisConstant;
import cn.lilemy.xiaoxinshu.mapper.QuestionMapper;
import cn.lilemy.xiaoxinshu.model.dto.question.QuestionViewNumRequest;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 定时将 Redis 中缓存的数据插入到 MySQL 数据库中
 */
@Slf4j
@Component
public class IncSyncQuestionViewNum {

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 定时任务：同步 Redis 中的浏览量数据到数据库
     */
    @Scheduled(fixedRate = 60 * 1000) // 每分钟执行一次
    public void syncViewCountToDatabase() {
        // 获取所有浏览量 Key
        String RedisKeys = String.format("%s:*", RedisConstant.QUESTION_VIEW_NUM_REDIS_KEY_PREFIX);
        Set<String> keys = stringRedisTemplate.keys(RedisKeys);
        if (CollUtil.isEmpty(keys)) {
            log.info("no inc question view num - keys is empty");
            return;
        }
        // 使用 multiGet 批量获取值
        List<String> values = stringRedisTemplate.opsForValue().multiGet(keys);
        if (CollUtil.isEmpty(values)) {
            log.info("no inc question view num - values is empty");
            return;
        }
        // 组合键值对
        List<QuestionViewNumRequest> questionViewNumRequestList = new ArrayList<>();
        Iterator<String> keyIterator = keys.iterator();
        Iterator<String> valueIterator = values.iterator();
        while (keyIterator.hasNext() && valueIterator.hasNext()) {
            QuestionViewNumRequest questionViewNumRequest = new QuestionViewNumRequest();
            String key = keyIterator.next();
            // 提取 questionId
            Long questionId = Long.parseLong(key.split(":")[2]);
            Integer value = Integer.valueOf(valueIterator.next());
            questionViewNumRequest.setViewNum(value);
            questionViewNumRequest.setId(questionId);
            questionViewNumRequestList.add(questionViewNumRequest);
        }
        log.info("questionViewNumRequestList:{}", questionViewNumRequestList);
        // 批量更新浏览量
        questionMapper.batchUpdateViewNum(questionViewNumRequestList);
        // 删除对应 key
        stringRedisTemplate.delete(keys);
    }
}
