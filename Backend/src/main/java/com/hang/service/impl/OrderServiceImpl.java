package com.hang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.entity.Orders;
import com.hang.mapper.OrdersMapper;
import com.hang.service.OrdersService;
import org.springframework.stereotype.Service;

/**
 * (Order)表服务实现类
 *
 * @author makejava
 * @since 2023-08-26 09:20:18
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrdersMapper,Orders> implements OrdersService{
}
