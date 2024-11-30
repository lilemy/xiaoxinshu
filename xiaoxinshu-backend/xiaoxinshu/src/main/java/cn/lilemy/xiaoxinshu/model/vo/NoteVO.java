package cn.lilemy.xiaoxinshu.model.vo;

import cn.hutool.json.JSONUtil;
import cn.lilemy.xiaoxinshu.model.entity.Note;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 笔记脱敏信息
 */
@Data
public class NoteVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 7117837581692083653L;

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 图片
     */
    private String picture;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 浏览量
     */
    private Integer viewNum;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 标签列表
     */
    private List<String> tagList;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 创建用户信息
     */
    private UserVO user;

    /**
     * 封装类转对象
     *
     * @param noteVO 题目脱敏信息
     * @return 题目信息
     */
    public static Note voToObj(NoteVO noteVO) {
        if (noteVO == null) {
            return null;
        }
        Note note = new Note();
        BeanUtils.copyProperties(noteVO, note);
        List<String> tagList = noteVO.getTagList();
        note.setTags(JSONUtil.toJsonStr(tagList));
        return note;
    }

    /**
     * 对象转封装类
     *
     * @param note 题目信息
     * @return 题目脱敏信息
     */
    public static NoteVO objToVo(Note note) {
        if (note == null) {
            return null;
        }
        NoteVO noteVO = new NoteVO();
        BeanUtils.copyProperties(note, noteVO);
        noteVO.setTagList(JSONUtil.toList(note.getTags(), String.class));
        return noteVO;
    }
}
