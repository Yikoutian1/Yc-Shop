package com.hang.controller;



import com.hang.entity.Admin;
import com.hang.result.ResponseResult;
import com.hang.service.AdminService;
import com.hang.utils.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * (Admin)表控制层
 *
 * @author makejava
 * @since 2023-08-23 11:52:48
 */
@RestController
@RequestMapping("/admin/system/index")
public class AdminController{
    @Autowired
    private AdminService adminService;
    /**
     * 登录
     * @return
     */
    @PostMapping("/login")
    public ResponseResult login(HttpServletRequest request,
                                @RequestBody Admin admin) {
        return adminService.login(request,admin);
    }
    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("/info")
    public ResponseResult info(HttpServletRequest request) {
        // 获取请求头token字符串
        String token = request.getHeader("token");
        // 从token获取用户名称(username)
        String username = JwtHelper.getUsername(token);
        Map<String, Object> map = new HashMap<>();
        map.put("roles","["+username+"]");
        map.put("name",username);
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return ResponseResult.okResult(map);
    }
    /**
     * 退出
     * @return
     */
    @PostMapping("/logout")
    public ResponseResult logout(HttpServletRequest request){
        request.getSession().removeAttribute("admin");
        return ResponseResult.okResult("退出成功");
    }

    /**
     * 清除缓存
     * @return
     */
    @PostMapping("/clearCache")
    public ResponseResult clearCache(){
        return adminService.clearCache();
    }

}


