package com.hang.controller;



import com.hang.result.ResponseResult;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * (User)表控制层
 *
 * @author makejava
 * @since 2023-08-15 19:50:45
 */
@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class UserController{
    /**
     * 登录
     * @return
     */
    @PostMapping("/login")
    public ResponseResult login() {
        Map<String, Object> map = new HashMap<>();
        map.put("token","admin");
        return ResponseResult.okResult(map);
    }
    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("/info")
    public ResponseResult info() {
        Map<String, Object> map = new HashMap<>();
        map.put("roles","[admin]");
        map.put("name","admin");
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return ResponseResult.okResult(map);
    }
    /**
     * 退出
     * @return
     */
    @PostMapping("/logout")
    public ResponseResult logout(){
        return ResponseResult.okResult();
    }
}

