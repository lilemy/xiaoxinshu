package cn.lilemy.xiaoxinshu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lilemy.xiaoxinshu.model.entity.InterfaceInfo;
import cn.lilemy.xiaoxinshu.service.InterfaceInfoService;
import cn.lilemy.xiaoxinshu.mapper.InterfaceInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author qq233
* @description 针对表【interface_info(接口信息)】的数据库操作Service实现
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService{

}




