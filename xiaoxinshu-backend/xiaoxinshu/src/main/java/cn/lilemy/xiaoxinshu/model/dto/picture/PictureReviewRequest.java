package cn.lilemy.xiaoxinshu.model.dto.picture;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PictureReviewRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 5217796787805739509L;

    /**
     * id  
     */  
    private Long id;  
  
    /**  
     * 状态：0-待审核, 1-通过, 2-拒绝  
     */  
    private Integer reviewStatus;  
  
    /**  
     * 审核信息  
     */  
    private String reviewMessage;  

}
