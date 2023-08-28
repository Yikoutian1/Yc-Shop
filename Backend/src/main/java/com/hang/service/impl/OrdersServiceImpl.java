package com.hang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.entity.Orders;
import com.hang.mapper.OrdersMapper;
import com.hang.result.ResponseResult;
import com.hang.service.OrdersService;
import com.hang.vo.OrderDetailVo;
import com.hang.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Order)表服务实现类
 *
 * @author makejava
 * @since 2023-08-26 09:20:18
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Autowired
    private OrdersMapper ordersMapper;

    @Override
    public ResponseResult getOrderDetailInfo() {
        List<OrderDetailVo> orderDetailVos = ordersMapper.getOrderDetailInfo();
        return ResponseResult.okResult(orderDetailVos);
    }

    @Override
    public ResponseResult allPageList(Integer pageNum, Integer pageSize, Integer status) {
        Page<Orders> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        // 与前端约定 不等于201的时候才是根据状态筛选
        queryWrapper.eq(status != 201, Orders::getStatus, status);
        page(page, queryWrapper);
        List<Orders> records = page.getRecords();
        return ResponseResult.okResult(new PageVo(records, page.getTotal()));
    }

    @Override
    public ResponseResult queryOrderList(Integer pageNum, Integer pageSize, Long input) {
        Page<Orders> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(input != null, Orders::getOid, input);
        page(page, queryWrapper);
        List<Orders> records = page.getRecords();
        return ResponseResult.okResult(new PageVo(records, page.getTotal()));
    }

    @Override
    public ResponseResult changStatus(Integer status, Long id) {
        boolean flag = ordersMapper.changStatus(status, id);
        return flag ? ResponseResult.okResult() : ResponseResult.errorResult(201, "更新失败");
    }
}
