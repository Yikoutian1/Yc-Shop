package com.hang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hang.entity.Comment;
import com.hang.vo.CommentVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * (Comment)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-24 17:02:26
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    Boolean addComment(@Param("content") String content,@Param("dto") CommentVo commentInfo);
}

