package com.hang.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (OrderDetail)表实体类
 *
 * @author makejava
 * @since 2023-08-26 15:29:11
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("order_detail")
public class OrderDetail  {
    @TableId
    private Integer id;

    //订单编号
    private Long oid;
    //商品编号
    private Long gid;
    //用户id
    private Long uid;
    //用户名
    private String name;
    //商品数量
    private Integer num;
    //照片
    private String image;
    //商品价格
    private Double price;



}

