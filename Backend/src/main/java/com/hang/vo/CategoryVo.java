package com.hang.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName CategoryVo
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/8/15 21:11
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryVo {
    private Long id;
    //分类名
    private String name;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
}
