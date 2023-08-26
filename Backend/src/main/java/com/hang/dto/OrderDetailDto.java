package com.hang.dto;

import com.hang.entity.ShoppingCart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName OrderDetailDto
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/8/26 15:34
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDto {
    private Long uid;
    private Long oid;
    private List<ShoppingCartDto> shop;
}
