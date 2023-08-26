package com.hang.vo;

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
public class CommentVo {
    private Long id;
    // 评论的人的名字
    private String nikeName;
    //用户评论的用户
    private Long uid;
    //评论的商品编号
    private Long shopId;
    private Integer star;
    //默认-1代表根评论，非-1则此处填用户id
    private Long rootId;
    //评论文字部分
    private String content;
    //照片列表
    private String images;

    private Date createTime;

    private List<CommentVo> children;
}
