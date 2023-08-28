package com.hang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hang.dto.OrderDetailDto;
import com.hang.entity.OrderDetail;
import com.hang.result.ResponseResult;


/**
 * (OrderDetail)表服务接口
 *
 * @author makejava
 * @since 2023-08-26 15:29:11
 */
public interface OrderDetailService extends IService<OrderDetail> {

    ResponseResult insertOrderDetail(OrderDetailDto orderDetailDto);

    ResponseResult searchOrders(Integer month);
}

