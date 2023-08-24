package com.hang.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName PageVo
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/8/24 17:59
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageVo {
    private List row;
    private Long total;
}
