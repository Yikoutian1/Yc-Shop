package com.hang.dto;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class ShoppingCartDto {
    @TableId
    private Long id;
    //商品id
    private Long gid;
    //商品名
    private String name;
    //商品价格
    private Double price;
    //商品数量
    private Integer num;
    //商品图片
    private String image;

}

