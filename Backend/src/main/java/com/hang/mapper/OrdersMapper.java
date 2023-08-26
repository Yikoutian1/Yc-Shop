package com.hang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hang.entity.Orders;
import org.apache.ibatis.annotations.Mapper;


/**
 * (Order)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-26 09:20:18
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

}
