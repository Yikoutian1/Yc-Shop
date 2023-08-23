package com.hang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hang.entity.Admin;
import org.apache.ibatis.annotations.Mapper;


/**
 * (Admin)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-23 11:52:48
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {

}

