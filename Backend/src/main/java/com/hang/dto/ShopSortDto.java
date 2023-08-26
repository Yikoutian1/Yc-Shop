package com.hang.dto;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName CategoryUpdateDto
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/8/16 21:03
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopSortDto {
    private Integer pageSize;
    private Integer pageNum;
    // 分类id
    private Long categoryId;
    // 价格区间
    private Double start;
    private Double end;
    // 通过销量
    private Integer sort;

}
