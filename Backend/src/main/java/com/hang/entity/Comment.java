package com.hang.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Comment)表实体类
 *
 * @author makejava
 * @since 2023-08-24 17:50:32
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("comment")
public class Comment implements Serializable{
    @TableId
    private Long id;

    //用户评论的用户
    private Long uid;
    //评论的商品编号
    private Long shopId;
    //默认-1代表根评论，非-1则此处填用户id
    private Long rootId;
    //评论文字部分
    private String content;
    //照片列表
    private String images;
    //评论时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;



}

