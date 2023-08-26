package com.hang.controller;



import com.hang.dto.OrderDetailDto;
import com.hang.entity.OrderDetail;
import com.hang.result.ResponseResult;
import com.hang.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * (OrderDetail)表控制层
 *
 * @author makejava
 * @since 2023-08-26 15:29:11
 */
@RestController
@RequestMapping("/orderDetail")
public class OrderDetailController{
    @Autowired
    private OrderDetailService orderDetailService;
    @PostMapping("/insertOrderDetail")
    public ResponseResult insertOrderDetail(@RequestBody OrderDetailDto orderDetailDto){
        return orderDetailService.insertOrderDetail(orderDetailDto);
    }
}

