package com.hang.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * @ClassName ShopInfoVo
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/8/18 18:59
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopInfoVo {
    private Long id;
    //商品名
    private String name;
    //商品库存
    private Long inventory;
    //商品价格w
    private Double price;
    //商品描述
    private String describle;
    //商品状态 1:默认起售 0:未起售
    private Integer status;

    private List<String> images;
    private Long sales;
    private Long categoryId;

}
