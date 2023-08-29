package com.hang.vo;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
public class UserVo {
    private Long id;

    //用户昵称
    private String nickName;
    //密码
//    private String password;
    //用户邮箱
    private String email;
    //性别 0:女 1:男
    private Integer gender;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //用户头像
    private String image;

    private String address;
    //0:禁用 1：启用
    private Integer status;

}

