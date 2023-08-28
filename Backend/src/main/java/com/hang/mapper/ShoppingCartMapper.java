package com.hang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hang.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;


/**
 * (ShoppingCart)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-28 19:55:59
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

}

