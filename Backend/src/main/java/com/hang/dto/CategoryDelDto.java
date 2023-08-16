package com.hang.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName CategoryDelDto
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/8/16 22:14
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDelDto {
    private List<Long> ids;
}
