package com.hang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.entity.Admin;
import com.hang.mapper.AdminMapper;
import com.hang.result.ResponseResult;
import com.hang.service.AdminService;
import com.hang.utils.JwtHelper;
import com.hang.utils.PasswordUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * (Admin)表服务实现类
 *
 * @author makejava
 * @since 2023-08-23 11:52:48
 */
@Service("adminService")
@Slf4j
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Override
    public ResponseResult login(HttpServletRequest request, Admin admin) {
        String password = admin.getPassword();
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername,admin.getUsername());
        Admin ad = getOne(queryWrapper);
        if(Objects.isNull(ad)){
            return ResponseResult.errorResult(520,"用户名或密码错误"); // 用户不存在
        }
        if(!PasswordUtils.check(password,ad.getPassword())){
            return ResponseResult.errorResult(521,"用户名或密码错误");
        }
        if(ad.getStatus() == 0){
            return ResponseResult.errorResult(523,"用户名或密码错误"); // 账号已禁用
        }
        log.info("密码匹配:{}",PasswordUtils.check(admin.getPassword(),ad.getPassword()));
        // 根据userid和username生成token字符串,通过map进行返回
        String token = JwtHelper.createToken(ad.getId().toString(), admin.getUsername());
        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        return ResponseResult.okResult(map);
    }
}

