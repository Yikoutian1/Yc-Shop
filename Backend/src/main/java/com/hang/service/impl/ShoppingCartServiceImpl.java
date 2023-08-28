package com.hang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.entity.ShoppingCart;
import com.hang.mapper.ShoppingCartMapper;
import com.hang.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * (ShoppingCart)表服务实现类
 *
 * @author makejava
 * @since 2023-08-28 19:55:59
 */
@Service("shoppingCartService")
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}

