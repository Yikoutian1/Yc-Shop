package com.hang.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Shop)表实体类
 *
 * @author makejava
 * @since 2023-08-25 16:33:53
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("shop")
public class Shop  {
    @TableId
    private Long id;

    //商品名
    private String name;
    //商品价格
    private Double price;
    //商品库存
    private Long inventory;
    //商品销量
    private Long sales;
    //评分
    private Float star;
    //商品照片
    private String image;
    //商品描述
    private String describle;
    //商品状态 1:默认起售 0:未起售
    private Integer status;
    //逻辑删除 1:删除 0:未删除
    private Integer delFlag;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;



}

