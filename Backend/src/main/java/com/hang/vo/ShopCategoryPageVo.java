package com.hang.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName ShopCategoryPageVo
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/8/23 1:51
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopCategoryPageVo {
    private List<ShopExistTableVo> list;
    private Integer total;
}
