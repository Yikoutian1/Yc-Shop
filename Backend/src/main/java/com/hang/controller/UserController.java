package com.hang.controller;



import com.hang.dto.UserDto;
import com.hang.entity.User;
import com.hang.result.ResponseResult;
import com.hang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * (User)表控制层
 *
 * @author makejava
 * @since 2023-08-15 19:50:45
 */
@RestController
@RequestMapping("/user")
public class UserController{
    @Autowired
    private UserService userService;
    @PostMapping("/addUser")
    public ResponseResult addUser(@RequestBody UserDto userDto){
        return userService.addUser(userDto);
    }
    @PostMapping("/updateUser")
    public ResponseResult updateUser(@RequestBody User user){
        return userService.updateById(user) ? ResponseResult.okResult(user) : ResponseResult.errorResult(530,"更新用户信息失败");
    }

    /**
     * 现在还用不了
     * @param email
     * @return
     */
    @GetMapping("/sendMsg/{email}")
    public ResponseResult sendMsg(@PathVariable("email") String email){
        return userService.sendMsg(email);
    }

}

