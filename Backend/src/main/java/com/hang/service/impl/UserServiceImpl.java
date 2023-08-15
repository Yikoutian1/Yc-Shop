package com.hang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.entity.User;
import com.hang.mapper.UserMapper;
import com.hang.service.UserService;
import org.springframework.stereotype.Service;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2023-08-15 19:50:50
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}

