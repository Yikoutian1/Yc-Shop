package com.hang.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (User)表实体类
 *
 * @author makejava
 * @since 2023-08-15 19:50:46
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User  {
    //主键@TableId
    private Long id;

    //用户昵称
    private String nickName;
    //用户邮箱
    private String email;
    //密码
    private String password;
    //1:用户 0管理员
    private Integer status;
    //用户头像
    private String image;
    //性别 0:女 1:男
    private Integer gender;



}

