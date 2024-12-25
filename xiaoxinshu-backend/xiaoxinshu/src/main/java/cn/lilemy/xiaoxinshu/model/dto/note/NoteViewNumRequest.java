package cn.lilemy.xiaoxinshu.model.dto.note;

import lombok.Data;

@Data
public class NoteViewNumRequest {

    /**
     * id
     */
    private Long id;

    /**
     * 浏览量
     */
    private Integer viewNum;
}
