package cn.lilemy.xiaoxinshu.model.dto.picture;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 图片上传请求体
 */
@Data
public class PictureUploadRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -8686942653213327509L;

    /**
     * 图片 id（用于修改）
     */
    private Long id;

    /**
     * 文件地址
     */
    private String fileUrl;

    /**
     * 图片名称
     */
    private String picName;

    /**
     * 空间 id（为空表示公共空间）
     */
    private Long spaceId;

}
