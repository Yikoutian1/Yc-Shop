package com.hang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hang.entity.OrderDetail;
import com.hang.vo.OrdersDataVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * (OrderDetail)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-26 15:29:11
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

    List<OrdersDataVo> searchSales(@Param("month") Integer month);
}

