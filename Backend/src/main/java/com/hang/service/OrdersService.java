package com.hang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hang.entity.Orders;
import com.hang.result.ResponseResult;


/**
 * (Order)表服务接口
 *
 * @author makejava
 * @since 2023-08-26 09:20:18
 */
public interface OrdersService extends IService<Orders> {

    ResponseResult getOrderDetailInfo();

    ResponseResult allPageList(Integer pageNum, Integer pageSize,Integer status);

    ResponseResult queryOrderList(Integer pageNum, Integer pageSize, Long input);

    ResponseResult changStatus(Integer status, Long id);

    ResponseResult searchSales(Integer month);

    ResponseResult circleEcharts();
}

