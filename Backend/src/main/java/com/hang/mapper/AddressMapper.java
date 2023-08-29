package com.hang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hang.entity.Address;
import org.apache.ibatis.annotations.Mapper;


/**
 * (Address)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-29 16:10:04
 */
@Mapper
public interface AddressMapper extends BaseMapper<Address> {

}

