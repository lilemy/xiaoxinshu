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
 * 个人笔记脱敏信息
 */
@Data
public class NotePersonalVO implements Serializable {
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
     * 可见范围(0-公开, 1-仅对自己可见)
     */
    private Integer visible;

    /**
     * 是否置顶(0-未置顶 1-置顶)
     */
    private Integer isTop;

    /**
     * 编辑时间
     */
    private Date editTime;

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
     * 状态：0-待审核, 1-通过, 2-拒绝
     */
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;

    /**
     * 审核人 id
     */
    private Long reviewerId;

    /**
     * 审核时间
     */
    private Date reviewTime;

    /**
     * 所属模块列表
     */
    private List<CategoriesVO> categoriesVOList;

    /**
     * 封装类转对象
     *
     * @param notePersonalVO 笔记脱敏信息
     * @return 笔记信息
     */
    public static Note voToObj(NotePersonalVO notePersonalVO) {
        if (notePersonalVO == null) {
            return null;
        }
        Note note = new Note();
        BeanUtils.copyProperties(notePersonalVO, note);
        List<String> tagList = notePersonalVO.getTagList();
        note.setTags(JSONUtil.toJsonStr(tagList));
        return note;
    }

    /**
     * 对象转封装类
     *
     * @param note 笔记信息
     * @return 笔记脱敏信息
     */
    public static NotePersonalVO objToVo(Note note) {
        if (note == null) {
            return null;
        }
        NotePersonalVO noteVO = new NotePersonalVO();
        BeanUtils.copyProperties(note, noteVO);
        noteVO.setTagList(JSONUtil.toList(note.getTags(), String.class));
        return noteVO;
    }
}
