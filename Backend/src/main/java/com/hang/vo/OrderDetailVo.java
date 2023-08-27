package com.hang.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName OrderDetailVo
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/8/27 11:08
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailVo {
    private Integer id;
    // 订单号
    private Long oid;
    // 地址
    private String address;
    // 商品名
    private String name;
    // 价格
    private Double price;
    // username   <-- a.name
    private String username;
    private Long phone;
    private String image;
    private Integer num;
}
