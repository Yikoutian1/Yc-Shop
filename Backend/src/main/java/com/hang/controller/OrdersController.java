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
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    @GetMapping("/getAllOrder")
    public ResponseResult getAllOrder(@RequestParam("pageNum") Integer pageNum,
                                      @RequestParam("pageSize") Integer pageSize,
                                      @RequestParam("status") Integer status
    ) {
        return ordersService.allPageList(pageNum, pageSize, status);
    }

    @GetMapping("/getOrderDetailInfo")
    public ResponseResult getOrderDetailInfo() {
        return ordersService.getOrderDetailInfo();
    }
    /**
     * 搜索
     * @param pageNum
     * @param pageSize
     * @param input
     * @return
     */
    @GetMapping("/queryOrderList")
    public ResponseResult queryOrderList(@RequestParam("pageNum") Integer pageNum,
                                         @RequestParam("pageSize") Integer pageSize,
                                         @RequestParam("input") Long input
    ) {
        return ordersService.queryOrderList(pageNum,pageSize,input);
    }
    @GetMapping("/changStatus")
    public ResponseResult changStatus(@RequestParam("status") Integer status,
                                      @RequestParam("id") Long id){
        return ordersService.changStatus(status,id);
    }
}

