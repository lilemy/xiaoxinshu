package cn.lilemy.xiaoxinshu.service;

import cn.lilemy.xiaoxinshu.model.dto.space.SpaceCreateRequest;
import cn.lilemy.xiaoxinshu.model.dto.space.SpaceEditRequest;
import cn.lilemy.xiaoxinshu.model.dto.space.SpaceQueryRequest;
import cn.lilemy.xiaoxinshu.model.dto.space.SpaceUpdateRequest;
import cn.lilemy.xiaoxinshu.model.vo.SpaceVO;
import cn.lilemy.xiaoxinshucommon.model.entity.Space;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author qq233
 * @description 针对表【space(空间)】的数据库操作Service
 */
public interface SpaceService extends IService<Space> {

    /**
     * 空间参数校验
     *
     * @param space 空间对象
     * @param add   是否是创建
     */
    void validSpace(Space space, boolean add);

    /**
     * 填充空间级别
     *
     * @param space 空间对象
     */
    void fillSpaceBySpaceLevel(Space space);

    /**
     * 空间创建请求
     *
     * @param spaceCreateRequest 空间创建请求
     * @return 新空间 id
     */
    Long createSpace(SpaceCreateRequest spaceCreateRequest);

    /**
     * 更新请求（仅管理员）
     *
     * @param spaceUpdateRequest 空间更新请求
     * @return 是否更新成功
     */
    Boolean updateSpace(SpaceUpdateRequest spaceUpdateRequest);

    /**
     * 编辑请求
     *
     * @param spaceEditRequest 空间编辑请求
     * @return 是否编辑成功
     */
    Boolean editSpace(SpaceEditRequest spaceEditRequest);

    /**
     * 获取空间查询对象
     *
     * @param spaceQueryRequest 空间查询请求
     * @return 空间查询对象
     */
    QueryWrapper<Space> getQueryWrapper(SpaceQueryRequest spaceQueryRequest);

    /**
     * 获取空间包装类（单条）
     *
     * @param space 空间实体类
     * @return 空间包装类
     */
    SpaceVO getSpaceVO(Space space);

    /**
     * 获取空间包装类（分页）
     *
     * @param spacePage 空间实体类
     * @return 空间包装类
     */
    Page<SpaceVO> getSpaceVOPage(Page<Space> spacePage);

}
