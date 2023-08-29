package com.hang.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Address)表实体类
 *
 * @author makejava
 * @since 2023-08-29 16:10:04
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("address")
public class Address  {
    //地址id@TableId
    private Integer id;

    //用户id
    private Long uid;
    //收货人
    private String name;
    //电话
    private String phone;
    //收货地址
    private String address;
    //1:设置默认 0:不是默认
    private String defaultAddress;



}

