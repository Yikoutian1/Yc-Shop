package com.hang.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @ClassName CircleEchartsVo
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/8/28 23:02
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CircleEchartsVo {
    private String name;
    private BigDecimal value;
}
