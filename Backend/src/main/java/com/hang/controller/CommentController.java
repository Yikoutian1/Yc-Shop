package com.hang.controller;



import com.hang.dto.CommentDto;
import com.hang.result.ResponseResult;
import com.hang.service.CommentService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * (Comment)表控制层
 *
 * @author makejava
 * @since 2023-08-24 17:02:26
 */
@RestController
@RequestMapping("/comment")
public class CommentController{
    @Autowired
    private CommentService commentService;
    @GetMapping("/queryAll")
    public ResponseResult queryAll(){
        return ResponseResult.okResult(commentService.list());
    }

    /**
     * 评论构建
     * @param pageNum 页码
     * @param pageSize 一页大小
     * @param shopId 商品id
     * @return 评论树
     */
    @GetMapping("/queryCommentList")
    public ResponseResult queryCommentList(@RequestParam("pageNum") Integer pageNum,@RequestParam("pageSize")Integer pageSize,@RequestParam("shopId")Long shopId){
        return commentService.queryCommentList(pageNum,pageSize,shopId);
    }
    @PostMapping("/confirmToComment")
    public ResponseResult confirmToComment(@RequestBody CommentDto commentDto){
        return commentService.addComment(commentDto);
    }
}

