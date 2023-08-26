package com.hang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.dto.OrderDetailDto;
import com.hang.dto.ShoppingCartDto;
import com.hang.entity.OrderDetail;
import com.hang.mapper.OrderDetailMapper;
import com.hang.result.ResponseResult;
import com.hang.service.OrderDetailService;
import com.hang.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.util.List;

/**
 * (OrderDetail)表服务实现类
 *
 * @author makejava
 * @since 2023-08-26 15:29:11
 */
@Service("orderDetailService")
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;
    @Autowired
    private TransactionDefinition transactionDefinition;
    @Override
    public ResponseResult insertOrderDetail(OrderDetailDto orderDetailDto) {
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        Long oid = orderDetailDto.getOid();
        Long uid = orderDetailDto.getUid();
        List<ShoppingCartDto> shoppingCartDtos = orderDetailDto.getShop();
        try {
            List<OrderDetail> orderDetails = BeanCopyUtils.copyBeanList(shoppingCartDtos, OrderDetail.class);
            for (OrderDetail detail : orderDetails) {
                detail.setOid(oid);
                detail.setUid(uid);
                baseMapper.insert(detail);
            }
            dataSourceTransactionManager.commit(transactionStatus);// 手动commit
            return ResponseResult.okResult();
        } catch (Exception e) {
            dataSourceTransactionManager.rollback(transactionStatus);
            return ResponseResult.errorResult(201, "新增失败");
        }
    }
}

