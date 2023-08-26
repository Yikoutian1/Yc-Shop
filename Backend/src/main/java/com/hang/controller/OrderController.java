package com.hang.controller;



import com.hang.result.ResponseResult;

import com.hang.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * (Order)表控制层
 *
 * @author makejava
 * @since 2023-08-26 09:20:18
 */
@RestController
@RequestMapping("/orders")
public class OrderController{
    @Autowired
    private OrdersService ordersService;
    @GetMapping("/getAllOrder")
    public ResponseResult getAllOrder(){
        return ResponseResult.okResult(ordersService.list());
    }
}

