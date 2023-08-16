package com.hang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hang.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * (Category)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-15 21:09:28
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    void updateStatusBatch(@Param("status") Integer status,@Param("ids") List<Long> ids);

    int countIsToDel(@Param("ids") List<Long> ids);
}

