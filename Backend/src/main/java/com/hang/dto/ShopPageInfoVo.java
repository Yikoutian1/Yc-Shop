package com.hang.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ShopPageInfoVo
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/8/23 1:07
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopPageInfoVo {
    private Integer pageNum;
    private Integer pageSize;
    private String categorySelect;
}
