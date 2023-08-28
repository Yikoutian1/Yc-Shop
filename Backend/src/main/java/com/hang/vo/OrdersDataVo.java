package com.hang.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @ClassName OrderSalesVo
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/8/28 16:01
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersDataVo {
    private Integer day;
    private BigDecimal num;
}
