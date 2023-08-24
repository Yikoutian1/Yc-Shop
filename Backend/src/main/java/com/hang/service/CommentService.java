package com.hang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hang.entity.Comment;
import com.hang.result.ResponseResult;
import org.apache.ibatis.annotations.Param;


/**
 * (Comment)表服务接口
 *
 * @author makejava
 * @since 2023-08-24 17:02:26
 */
public interface CommentService extends IService<Comment> {

    ResponseResult queryCommentList(@Param("pageNum") Integer pageNum,@Param("pageSize") Integer pageSize,@Param("shopId") Long shopId);
}

