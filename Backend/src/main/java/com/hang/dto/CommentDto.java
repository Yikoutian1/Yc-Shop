package com.hang.dto;

import com.hang.vo.CommentVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @ClassName CommentVo
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/8/24 17:22
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private CommentVo commentInfo;
    //评论文字部分
    private String content;

}
