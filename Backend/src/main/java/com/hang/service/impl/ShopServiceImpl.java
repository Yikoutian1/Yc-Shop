package com.hang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.dto.ShopInfoVo;
import com.hang.entity.Shop;
import com.hang.mapper.ShopMapper;
import com.hang.result.ResponseResult;
import com.hang.service.ShopService;
import com.hang.service.UploadService;
import com.hang.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;

/**
 * (Shop)表服务实现类
 *
 * @author makejava
 * @since 2023-08-17 19:27:37
 */
@Service("shopService")
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements ShopService {
    @Autowired
    private UploadService uploadService;
    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;
    @Autowired
    private TransactionDefinition transactionDefinition;
    @Override
    public ResponseResult addShopInfo(ShopInfoVo shopInfoVo) {
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        Shop shop = BeanCopyUtils.copyBean(shopInfoVo, Shop.class);
        String images = shopInfoVo.getImages()
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));;
                shop.setImage(images);
        try{
            // 商品表
            baseMapper.insert(shop);
            Long id = shop.getId();
            // 商品 分类表
            baseMapper.insertIntoShopCategory(shopInfoVo,id);
            dataSourceTransactionManager.commit(transactionStatus);// 手动commit
            return ResponseResult.okResult();
        }catch (Exception e) {
            dataSourceTransactionManager.rollback(transactionStatus);
            return ResponseResult.errorResult(201, "新增失败");
        }
    }
}

