package com.lilemy.xiaoxinshu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lilemy.xiaoxinshu.mapper.UserMapper;
import com.lilemy.xiaoxinshu.model.entity.User;
import com.lilemy.xiaoxinshu.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author lilemy
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2025-08-13 11:34:58
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

}




