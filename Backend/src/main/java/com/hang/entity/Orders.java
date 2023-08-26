package com.hang.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Order)表实体类
 *
 * @author makejava
 * @since 2023-08-26 09:20:18
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("orders")
public class Orders  {
    @TableId
    private Integer id;

    //订单编号
    private Long oid;
    //地址编号
    private Long aid;
    //支付宝交易号
    private Long payId;
    //支付时间
    private Date payTime;
    //订单创建时间
    private Date createTime;
    //0:待付款 1:待发货 2:已发货 3:已收货 4:取消
    private Integer status;



}

