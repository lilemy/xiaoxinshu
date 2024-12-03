package cn.lilemy.xiaoxinshu.service.impl;

import cn.lilemy.xiaoxinshu.mapper.NoteCategoriesMapper;
import cn.lilemy.xiaoxinshu.model.entity.NoteCategories;
import cn.lilemy.xiaoxinshu.service.NoteCategoriesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
* @author qq233
* @description 针对表【note_categories(笔记分类关系)】的数据库操作Service实现
*/
@Service
public class NoteCategoriesServiceImpl extends ServiceImpl<NoteCategoriesMapper, NoteCategories>
    implements NoteCategoriesService{

}




