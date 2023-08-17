package com.hang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.entity.Shop;
import com.hang.mapper.ShopMapper;
import com.hang.service.ShopService;
import org.springframework.stereotype.Service;

/**
 * (Shop)表服务实现类
 *
 * @author makejava
 * @since 2023-08-17 19:27:37
 */
@Service("shopService")
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements ShopService {

}

