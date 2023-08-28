package com.hang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.dto.OrderDetailDto;
import com.hang.dto.ShoppingCartDto;
import com.hang.entity.OrderDetail;
import com.hang.mapper.OrderDetailMapper;
import com.hang.mapper.OrdersMapper;
import com.hang.mapper.ShopMapper;
import com.hang.mapper.ShoppingCartMapper;
import com.hang.result.ResponseResult;
import com.hang.service.OrderDetailService;
import com.hang.utils.BeanCopyUtils;
import com.hang.utils.RedisCache;
import com.hang.vo.OrderSalesVo;
import com.hang.vo.OrdersDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult insertOrderDetail(OrderDetailDto orderDetailDto) {
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        Long oid = orderDetailDto.getOid();
        Long uid = orderDetailDto.getUid();
        List<ShoppingCartDto> shoppingCartDtos = orderDetailDto.getShop();
        try {
            List<OrderDetail> orderDetails = BeanCopyUtils.copyBeanList(shoppingCartDtos, OrderDetail.class);
            List<Integer> ids = new ArrayList<>();
            for (OrderDetail detail : orderDetails) {
                detail.setOid(oid);
                detail.setUid(uid);
                baseMapper.insert(detail);
                ids.add(detail.getId());
            }
            shoppingCartMapper.deleteBatchIds(ids);
            dataSourceTransactionManager.commit(transactionStatus);// 手动commit
            return ResponseResult.okResult();
        } catch (Exception e) {
            dataSourceTransactionManager.rollback(transactionStatus);
            return ResponseResult.errorResult(201, "新增失败");
        }
    }

    @Override
    public ResponseResult searchOrders(Integer month) {
        List<OrdersDataVo> list = null;
        List<OrdersDataVo> vo = null;
        String searchOrders = "Draw:searchOrders_" + month;
        vo = redisCache.getCacheList(searchOrders);
        if(vo.size() != 0){
            return ResponseResult.okResult(vo);
        }
        vo = new ArrayList<>();
        list = ordersMapper.searchOrders(month);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int daysInMonth = getDaysInMonth(currentYear, month); // 获取指定月份的天数
        for (int day = 1; day <= daysInMonth; day++) {
            boolean found = false;
            for (OrdersDataVo data : list) {
                if (data.getDay() == day) {
                    vo.add(data);
                    found = true;
                    break;
                }
            }
            if (!found) {
                vo.add(new OrdersDataVo(day, BigDecimal.ZERO)); // 添加缺失的日期，销售额为0
            }
        }
        redisCache.setCacheList(searchOrders,vo);
        redisCache.expire(searchOrders,12, TimeUnit.HOURS);
        return ResponseResult.okResult(vo);
    }

    private static int getDaysInMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1); // 月份在 Calendar 类中是从0开始的，所以需要减1
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}

