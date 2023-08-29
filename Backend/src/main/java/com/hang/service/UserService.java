package com.hang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hang.dto.UserDto;
import com.hang.entity.User;
import com.hang.result.ResponseResult;


/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2023-08-15 19:50:49
 */
public interface UserService extends IService<User> {

    ResponseResult addUser(UserDto userDto);

    ResponseResult sendMsg(String email);

    ResponseResult queryUserWithPageInfo(Integer pageNum, Integer pageSize, String input);

    ResponseResult handleUserStatus(Long id, Integer status);

}

