package com.hang.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName PageDto
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/8/16 16:25
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDto {
    private Integer pageNum;
    private Integer pageSize;
}
