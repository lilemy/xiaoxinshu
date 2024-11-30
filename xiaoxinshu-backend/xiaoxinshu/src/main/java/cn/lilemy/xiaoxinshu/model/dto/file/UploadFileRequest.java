package cn.lilemy.xiaoxinshu.model.dto.file;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件上传请求
 */
@Data
public class UploadFileRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 4863853687199240980L;

    /**
     * 业务
     */
    private String biz;

    /**
     * 业务 id
     */
    private Long bizId;

}