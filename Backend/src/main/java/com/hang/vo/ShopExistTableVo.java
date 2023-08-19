package com.hang.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName ShopExistTableVo
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/8/19 16:01
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopExistTableVo {
    private Long id;

    //商品名
    private String name;
    //商品价格
    private Double price;
    //商品销量
    private Long sales;
    //商品照片
    private String image;
    //商品描述
    private String categoryName;

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
