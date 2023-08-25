package com.hang.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @ClassName ShopVo
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/8/19 11:13
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopVo {
    private Long id;
    //商品名
    private String name;
    //商品价格
    private Double price;
    //商品照片
    private String image;

    private Float star;
    //商品描述
    private String describle;
    //商品库存
    private Long inventory;

    private Long sales;

    private List<String> images;
}
