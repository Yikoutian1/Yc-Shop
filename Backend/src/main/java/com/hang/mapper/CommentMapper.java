package com.hang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hang.entity.Comment;
import org.apache.ibatis.annotations.Mapper;


/**
 * (Comment)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-24 17:02:26
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}

