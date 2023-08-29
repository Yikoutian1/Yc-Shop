package com.hang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hang.entity.Admin;
import com.hang.result.ResponseResult;

import javax.servlet.http.HttpServletRequest;


/**
 * (Admin)表服务接口
 *
 * @author makejava
 * @since 2023-08-23 11:52:48
 */
public interface AdminService extends IService<Admin> {

    ResponseResult login(HttpServletRequest request, Admin admin);

    ResponseResult clearCache();
}

