package com.hang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.dto.UserDto;
import com.hang.entity.User;
import com.hang.mapper.UserMapper;
import com.hang.result.ResponseResult;
import com.hang.service.UserService;
import com.hang.utils.EmailUtils;
import com.hang.utils.RandomStringGenerator;
import com.hang.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2023-08-15 19:50:50
 */
@Slf4j
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MailService mailService;

    @Override
    public ResponseResult addUser(UserDto userDto) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName, userDto.getUser().getNickName());
        User one = getOne(queryWrapper);
        if (one != null) {
            return ResponseResult.errorResult(531,"该用户名已存在");
        }
        // 获取 redis缓存的验证码
        String code = null;
        code = (String) redisTemplate.opsForValue().get(userDto.getUser().getEmail());
        // 如果验证码为空
        if(userDto.getCode() == null){
            return ResponseResult.errorResult(534,"验证码不能为空");
        }
        // 通过比对
        if(userDto.getCode().equals(code)){
            // 生成随机的字符串
            String s = RandomStringGenerator.generateRandomString();
            userDto.getUser().setNickName(s);
            save(userDto.getUser());
            return ResponseResult.okResult("注册成功!");
        }
        return ResponseResult.errorResult(535,"注册失败");
    }

    @Override
    public ResponseResult sendMsg(String email) {
//        //生成随机的4位验证码
        String code = ValidateCodeUtils.generateValidateCode(4).toString();
        log.info("code={}",code);
        String redisCode = null;
        //调用阿里云提供的短信服务API完成发送短信
        //SMSUtils.sendMessage("瑞吉外卖","",phone,code);
        redisCode = (String) redisTemplate.opsForValue().get(email);
        if(redisCode != null){
            return ResponseResult.errorResult(533,"五分钟内不可重复发送验证码");
        }
        // 如果没有发送验证码
//        boolean b = EmailUtils.sendAuthCodeEmail(email, code);
        String msg = "尊敬的用户:你好!\n 您的注册验证码为:" + code+"\n"+"     (有效期为五分钟)";
        mailService.sendTextMailMessage(email,"验证码",msg);
        //存入Redis中,设置有效期为五分钟
        redisTemplate.opsForValue().set(email,code,5, TimeUnit.MINUTES);
        return ResponseResult.okResult("验证码发送成功,请注意接收");
    }
}

