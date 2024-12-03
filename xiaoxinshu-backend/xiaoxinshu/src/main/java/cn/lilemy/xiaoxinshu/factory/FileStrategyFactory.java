package cn.lilemy.xiaoxinshu.factory;

import cn.lilemy.xiaoxinshucommon.common.ResultCode;
import cn.lilemy.xiaoxinshucommon.exception.BusinessException;
import cn.lilemy.xiaoxinshu.strategy.FileStrategy;
import cn.lilemy.xiaoxinshu.strategy.impl.CosFileStrategy;
import cn.lilemy.xiaoxinshu.strategy.impl.MinioFileStrategy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 工厂模式：
 * 根据配置文件中的参数加载对应的 Bean
 */
@Configuration
public class FileStrategyFactory {

    @Value("${storage.file.type}")
    private String fileStrategyType = "minio";

    @Bean
    public FileStrategy getFileStrategy() {
        if (StringUtils.equals(fileStrategyType, "minio")) {
            return new MinioFileStrategy();
        } else if (StringUtils.equals(fileStrategyType, "cos")) {
            return new CosFileStrategy();
        }
        throw new BusinessException(ResultCode.PARAMS_ERROR, "不可用的存储类型");
    }
}
