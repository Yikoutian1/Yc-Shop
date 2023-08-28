package com.hang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hang.entity.Orders;
import com.hang.vo.OrderDetailVo;
import com.hang.vo.OrderSalesVo;
import com.hang.vo.OrdersDataVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * (Order)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-26 09:20:18
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

    List<OrderDetailVo> getOrderDetailInfo();

    boolean changStatus(@Param("status") Integer status,@Param("id") Long id);

    List<OrderSalesVo> searchSales(@Param("month") Integer month);

    List<OrdersDataVo> searchOrders(@Param("month") Integer month);
}

