package cn.lilemy.xiaoxinshu.mapper;

import cn.lilemy.xiaoxinshu.model.dto.note.NoteViewNumRequest;
import cn.lilemy.xiaoxinshu.model.dto.question.QuestionViewNumRequest;
import cn.lilemy.xiaoxinshucommon.model.entity.Note;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author qq233
 * @description 针对表【note(笔记)】的数据库操作Mapper
 * @Entity cn.lilemy.xiaoxinshu.model.entity.Note
 */
public interface NoteMapper extends BaseMapper<Note> {

    @Update("<script>" +
            "UPDATE note " +
            "SET view_num = view_num + CASE id " +
            "<foreach collection='list' item='item'>" +
            "WHEN #{item.id} THEN #{item.viewNum} " +
            "</foreach>" +
            "END " +
            "WHERE id IN " +
            "<foreach collection='list' item='item' open='(' separator=',' close=')'>" +
            "#{item.id}" +
            "</foreach>" +
            "</script>")
    void batchUpdateViewNum(@Param("list") List<NoteViewNumRequest> requestList);
}




