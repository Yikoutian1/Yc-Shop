package com.hang.vo;

import com.hang.entity.Shop;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName ShopPageVo
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/8/19 13:42
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopPageVo {
    private List<ShopExistTableVo> row;
    private Long total;
}
