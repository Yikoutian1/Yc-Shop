package com.hang.dto;

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
public class CategoryUpdateDto {
    private List<Long> ids;
    private Integer status;
}
