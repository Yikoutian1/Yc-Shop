package com.hang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hang.entity.Category;
import org.apache.ibatis.annotations.Mapper;


/**
 * (Category)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-15 21:09:28
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}

