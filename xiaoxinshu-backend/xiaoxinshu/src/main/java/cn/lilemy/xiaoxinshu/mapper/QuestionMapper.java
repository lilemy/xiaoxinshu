package cn.lilemy.xiaoxinshu.mapper;

import cn.lilemy.xiaoxinshu.model.dto.question.QuestionViewNumRequest;
import cn.lilemy.xiaoxinshucommon.model.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
* @author qq233
* @description 针对表【question(题目)】的数据库操作Mapper
* @Entity cn.lilemy.xiaoxinshu.model.entity.Question
*/
public interface QuestionMapper extends BaseMapper<Question> {

    /**
     * 查询题目列表（包括已被删除的数据）
     */
    @Select("select * from question where update_time >= #{minUpdateTime}")
    List<Question> listQuestionWithDelete(Date minUpdateTime);

    @Update("<script>" +
            "UPDATE question " +
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
    void batchUpdateViewNum(@Param("list") List<QuestionViewNumRequest> requestList);
}




