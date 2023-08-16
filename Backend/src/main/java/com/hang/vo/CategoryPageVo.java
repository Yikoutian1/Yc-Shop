package com.hang.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName CategoryPageVo
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/8/16 16:32
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryPageVo {
    private List<CategoryVo> row;
    private Long total;
}
