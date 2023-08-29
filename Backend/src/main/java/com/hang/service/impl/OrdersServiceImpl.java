package com.hang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.entity.Orders;
import com.hang.mapper.OrdersMapper;
import com.hang.result.ResponseResult;
import com.hang.service.OrdersService;
import com.hang.utils.RedisCache;
import com.hang.vo.CircleEchartsVo;
import com.hang.vo.OrderDetailVo;
import com.hang.vo.OrderSalesVo;
import com.hang.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * (Order)表服务实现类
 *
 * @author makejava
 * @since 2023-08-26 09:20:18
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult getOrderDetailInfo() {
        List<OrderDetailVo> orderDetailVos = ordersMapper.getOrderDetailInfo();
        return ResponseResult.okResult(orderDetailVos);
    }

    @Override
    public ResponseResult allPageList(Integer pageNum, Integer pageSize, Integer status) {
        Page<Orders> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        // 与前端约定 不等于201的时候才是根据状态筛选
        queryWrapper.eq(status != 201, Orders::getStatus, status);
        page(page, queryWrapper);
        List<Orders> records = page.getRecords();
        return ResponseResult.okResult(new PageVo(records, page.getTotal()));
    }

    @Override
    public ResponseResult queryOrderList(Integer pageNum, Integer pageSize, Long input) {
        Page<Orders> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(input != null, Orders::getOid, input);
        page(page, queryWrapper);
        List<Orders> records = page.getRecords();
        return ResponseResult.okResult(new PageVo(records, page.getTotal()));
    }

    @Override
    public ResponseResult changStatus(Integer status, Long id) {
        boolean flag = ordersMapper.changStatus(status, id);
        return flag ? ResponseResult.okResult() : ResponseResult.errorResult(201, "更新失败");
    }

    @Override
    public ResponseResult searchSales(Integer month) {
        List<OrderSalesVo> sales = null;
        List<OrderSalesVo> vo = null;
        String searchSales = "Draw:searchSales_" + month;
        vo = redisCache.getCacheList(searchSales);
        if(vo.size() != 0){
            return ResponseResult.okResult(vo);
        }
        vo = new ArrayList<>();
        sales = ordersMapper.searchSales(month);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int daysInMonth = getDaysInMonth(currentYear, month); // 获取指定月份的天数

        for (int day = 1; day <= daysInMonth; day++) {
            boolean found = false;
            for (OrderSalesVo data : sales) {
                if (data.getDay() == day) {
                    vo.add(data);
                    found = true;
                    break;
                }
            }
            if (!found) {
                vo.add(new OrderSalesVo(day, BigDecimal.ZERO)); // 添加缺失的日期，销售额为0
            }
        }
        redisCache.setCacheList(searchSales,vo);
        redisCache.expire(searchSales,12, TimeUnit.HOURS);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult circleEcharts() {
        List<CircleEchartsVo> vos = null;
        String circleEcharts = "Draw:circleEcharts";
        vos = redisCache.getCacheList(circleEcharts);
        if(vos.size()!=0){
            return ResponseResult.okResult(vos);
        }
        vos = ordersMapper.circleEcharts();
        redisCache.setCacheList(circleEcharts,vos);
        // 设置30分钟缓存
        redisCache.expire(circleEcharts,30,TimeUnit.MINUTES);
        return ResponseResult.okResult(vos);
    }

    private static int getDaysInMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1); // 月份在 Calendar 类中是从0开始的，所以需要减1
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}
