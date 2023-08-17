package com.hang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hang.entity.Shop;
import org.apache.ibatis.annotations.Mapper;


/**
 * (Shop)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-17 19:27:37
 */
@Mapper
public interface ShopMapper extends BaseMapper<Shop> {

}

