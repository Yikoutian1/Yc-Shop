package com.hang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.dto.CategoryUpdateDto;
import com.hang.dto.PageDto;
import com.hang.dto.ShopDto;
import com.hang.dto.ShopInfoVo;
import com.hang.entity.Category;
import com.hang.entity.Shop;
import com.hang.mapper.ShopMapper;
import com.hang.result.ResponseResult;
import com.hang.service.CategoryService;
import com.hang.service.ShopService;
import com.hang.utils.BeanCopyUtils;
import com.hang.vo.*;
import io.swagger.models.auth.In;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (Shop)表服务实现类
 *
 * @author makejava
 * @since 2023-08-17 19:27:37
 */
@Service("shopService")
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements ShopService {
    private final static String BaseImageUrl = "http://rzl9bicnx.hn-bkt.clouddn.com/";
    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;
    @Autowired
    private TransactionDefinition transactionDefinition;
    @Autowired
    private CategoryService categoryService;

    @Override
    public ResponseResult addShopInfo(ShopInfoVo shopInfoVo) {
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        Shop shop = BeanCopyUtils.copyBean(shopInfoVo, Shop.class);
        String images = shopInfoVo.getImages()
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        ;
        shop.setImage(images);
        try {
            // 商品表
            baseMapper.insert(shop);
            Long id = shop.getId();
            // 商品 分类表
            baseMapper.insertIntoShopCategory(shopInfoVo, id);
            dataSourceTransactionManager.commit(transactionStatus);// 手动commit
            return ResponseResult.okResult();
        } catch (Exception e) {
            dataSourceTransactionManager.rollback(transactionStatus);
            return ResponseResult.errorResult(201, "新增失败");
        }
    }

    @Override
    public ResponseResult delShopBatchByIds(List<Long> ids) {
        LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Shop::getStatus, 1) // 1:在售  0:未售卖
                .in(Shop::getId,ids);
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            return ResponseResult.errorResult(201, "当前选择的商品在售卖,不能删除");
        }else {
            // 可以删除
            baseMapper.deleteBatchIds(ids);
            return ResponseResult.okResult();
        }

    }
    @Override
    public ResponseResult changeShopStatusBatch(ShopDto shopDto) {
        Integer status = shopDto.getStatus();
        List<Long> ids = shopDto.getIds();
        baseMapper.updateStatusBatch(status, ids);
        return ResponseResult.okResult();
    }

    /**
     * 根据分类id查询商品
     * @param id
     * @return
     */
    @Override
    public ResponseResult selectShopByCategoryId(Long id) {
        List<ShopExistTableVo> shopExistTableVos = baseMapper.selectShopByCategoryId(id);
        return ResponseResult.okResult(shopExistTableVos);
    }

    @Override
    public ResponseResult updateShopById(ShopInfoVo shopInfoVo) {
        Shop shop = BeanCopyUtils.copyBean(shopInfoVo, Shop.class);
        // 集合转字符串
        String images = shopInfoVo.getImages()
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        ;
        shop.setImage(images);
        updateById(shop);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getShopList() {
        List<Shop> list = list();
        list.stream().map(item -> {
            String[] split = item.getImage().split(",");
            for (int i = 0; i < split.length; i++) {
                split[i] = BaseImageUrl + split[i];
            }
            String res = String.join(",", split);
            item.setImage(res);
            return item;
        }).collect(Collectors.toList());
        List<ShopVo> shopVos = BeanCopyUtils.copyBeanList(list, ShopVo.class);
        shopVos.stream().map(item -> {
            List<String> imagess = new ArrayList<>();
            String[] split = item.getImage().split(",");
            for (String s : split) {
                imagess.add(s);
            }
            item.setImages(imagess);
            return item;
        }).collect(Collectors.toList());
        return ResponseResult.okResult(shopVos);
    }
    @Override
    public ResponseResult getShopListByPageInfo(PageDto pageDto) {
//        Page<Shop> page = new Page<>(pageDto.getPageNum(), pageDto.getPageSize());
        Integer pageNum = (pageDto.getPageNum()-1) * pageDto.getPageSize();
        // 总数据
        Integer count=baseMapper.selectCount(null);
        // 总页数
        Integer total = count / pageDto.getPageSize();
        System.out.println(total);
        // size > 总数
        if(count <= pageDto.getPageSize()){
            total = 1;
        }
        List<ShopExistTableVo> shopExistTableVos = baseMapper.selectShopListAndCategoryName(pageNum, pageDto.getPageSize());
        shopExistTableVos.stream().map(item -> {
            String[] split = item.getImage().split(",");
            for (int i = 0; i < split.length; i++) {
                split[i] = BaseImageUrl + split[i];
            }
            String res = String.join(",", split);
            item.setImage(res);
            return item;
        }).collect(Collectors.toList());
        ShopPageVo pageVo = new ShopPageVo(shopExistTableVos, total.longValue());
        return ResponseResult.okResult(pageVo);
    }
    @Override
    public ResponseResult queryShopById(Long id) {
        List<String> imagess = new ArrayList<>();
        Shop item = getById(id);
        String[] split = item.getImage().split(",");
        for (String s : split) {
            s = BaseImageUrl + s;
            imagess.add(s);
        }
        ShopVo shopVo = BeanCopyUtils.copyBean(item, ShopVo.class);
        shopVo.setImages(imagess);
        return ResponseResult.okResult(shopVo);
    }

    @Override
    public ResponseResult searchByName(String name) {
        LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Shop::getName, name);
        List<Shop> shopList = list(queryWrapper);
        shopList.stream().map(item -> {
            String[] split = item.getImage().split(",");
            for (int i = 0; i < split.length; i++) {
                split[i] = BaseImageUrl + split[i];
            }
            String res = String.join(",", split);
            item.setImage(res);
            return item;
        }).collect(Collectors.toList());
        List<ShopExistTableVo> shopExistTableVo = categoryService.getCategoryNameList(shopList);
        return ResponseResult.okResult(shopExistTableVo);
    }


    private List<ShopVo> transArray(List<Shop> list) {
        list.stream().map(item -> {
            String[] split = item.getImage().split(",");
            for (int i = 0; i < split.length; i++) {
                split[i] = BaseImageUrl + split[i];
            }
            String res = String.join(",", split);
            item.setImage(res);
            return item;
        }).collect(Collectors.toList());
        List<ShopVo> shopVos = BeanCopyUtils.copyBeanList(list, ShopVo.class);
        shopVos.stream().map(item -> {
            List<String> imagess = new ArrayList<>();
            String[] split = item.getImage().split(",");
            for (String s : split) {
                imagess.add(s);
            }
            item.setImages(imagess);
            return item;
        }).collect(Collectors.toList());
        return shopVos;
    }

}

