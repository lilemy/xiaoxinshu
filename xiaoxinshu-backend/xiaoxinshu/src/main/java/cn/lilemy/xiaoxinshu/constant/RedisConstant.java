package cn.lilemy.xiaoxinshu.constant;

import cn.hutool.json.JSONUtil;
import org.springframework.util.DigestUtils;

/**
 * Redis 常量
 */
public interface RedisConstant {

    /**
     * 用户签到记录的 Redis Key 前缀
     */
    String USER_SIGN_IN_REDIS_KEY_PREFIX = "user:signins";

    /**
     * 获取用户签到记录的 Redis Key
     *
     * @param year   年份
     * @param userId 用户 id
     * @return 拼接好的 Redis Key
     */
    static String getUserSignInRedisKey(int year, long userId) {
        return String.format("%s:%s:%s", USER_SIGN_IN_REDIS_KEY_PREFIX, year, userId);
    }

    /**
     * 题目封装信息缓存的 Redis Key 前缀
     */
    String QUESTION_LIST_QUESTION_VO_BY_PAGE_REDIS_KEY_PREFIX = "question:listQuestionVOByPage";

    /**
     * 笔记封装信息缓存的 Redis Key 前缀
     */
    String NOTE_LIST_NOTE_VO_BY_PAGE_REDIS_KEY_PREFIX = "note:listNoteVOByPage";

    /**
     * 图片封装信息缓存的 Redis Key 前缀
     */
    String PICTURE_LIST_PICTURE_VO_BY_PAGE_REDIS_KEY_PREFIX = "picture:listPictureVOByPage";

    /**
     * 获取封装信息缓存的 Redis Key
     *
     * @param queryRequest 分页请求参数
     * @param prefix       前缀
     * @return 拼接好的 Redis Key
     */
    static String getListVoByPageRedisKey(Object queryRequest, String prefix) {
        String queryCondition = JSONUtil.toJsonStr(queryRequest);
        String hashKey = DigestUtils.md5DigestAsHex(queryCondition.getBytes());
        return String.format("%s:%s", prefix, hashKey);
    }

    /**
     * 题目浏览量缓存的 Redis Key 前缀
     */
    String QUESTION_VIEW_NUM_REDIS_KEY_PREFIX = "question:viewNum";

    /**
     * 笔记浏览量缓存的 Redis Key 前缀
     */
    String NOTE_VIEW_NUM_REDIS_KEY_PREFIX = "note:viewNum";

}
