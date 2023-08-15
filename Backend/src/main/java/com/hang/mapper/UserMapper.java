package com.hang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hang.entity.User;
import org.apache.ibatis.annotations.Mapper;


/**
 * (User)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-15 19:50:52
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}

