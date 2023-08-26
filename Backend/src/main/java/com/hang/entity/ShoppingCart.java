package com.hang.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (ShoppingCart)表实体类
 *
 * @author makejava
 * @since 2023-08-26 15:37:19
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("shopping_cart")
public class ShoppingCart  {
    @TableId
    private Long id;

    //用户编号
    private Long uid;
    //商品id
    private Long shopId;
    //商品名
    private String shopName;
    //商品价格
    private Double shopPrice;
    //商品数量
    private Integer shopNum;
    //商品图片
    private String shopImage;

}

