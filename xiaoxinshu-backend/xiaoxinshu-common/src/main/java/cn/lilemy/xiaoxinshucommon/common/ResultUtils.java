package cn.lilemy.xiaoxinshucommon.common;


/**
 * 返回工具类
 */
public class ResultUtils {

    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 失败
     *
     * @param resultCode
     * @return
     */
    public static <T> BaseResponse<T> error(ResultCode resultCode) {
        return new BaseResponse<>(resultCode);
    }

    /**
     * 失败
     *
     * @param code
     * @param message
     * @return
     */
    public static <T> BaseResponse<T> error(int code, String message) {
        return new BaseResponse<>(code, null, message);
    }

    /**
     * 失败
     *
     * @param resultCode
     * @return
     */
    public static <T> BaseResponse<T> error(ResultCode resultCode, String message) {
        return new BaseResponse<>(resultCode.getCode(), null, message);
    }

}
