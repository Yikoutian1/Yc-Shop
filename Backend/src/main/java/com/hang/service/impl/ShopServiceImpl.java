package com.hang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.dto.*;
import com.hang.emuns.StatusEnum;
import com.hang.entity.Category;
import com.hang.entity.Shop;
import com.hang.mapper.ShopMapper;
import com.hang.result.ResponseResult;
import com.hang.service.CategoryService;
import com.hang.service.ShopService;
import com.hang.utils.BeanCopyUtils;
import com.hang.utils.RedisCache;
import com.hang.vo.*;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * (Shop)表服务实现类
 *
 * @author makejava
 * @since 2023-08-17 19:27:37
 */
@Service("shopService")
@Slf4j
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements ShopService {
    private final static String BaseImageUrl = "http://rzl9bicnx.hn-bkt.clouddn.com/";
    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;
    @Autowired
    private TransactionDefinition transactionDefinition;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisCache redisCache;

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
            delShopAndCategoryAndTotal();
            return ResponseResult.okResult();
        } catch (Exception e) {
            dataSourceTransactionManager.rollback(transactionStatus);
            return ResponseResult.errorResult(201, "新增失败");
        }
    }

    @Override
    public ResponseResult delShopBatchByIds(List<Long> ids) {
        LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Shop::getStatus, StatusEnum.SHOP_BUYING) // 1:在售  0:未售卖
                .in(Shop::getId, ids);
        Integer count = baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            return ResponseResult.errorResult(201, "当前选择的商品在售卖,不能删除");
        } else {
            // 可以删除
            baseMapper.deleteBatchIds(ids);
            delShopAndCategoryAndTotal();
            return ResponseResult.okResult();
        }

    }

    @Override
    public ResponseResult changeShopStatusBatch(ShopDto shopDto) {
        Integer status = shopDto.getStatus();
        List<Long> ids = shopDto.getIds();
        baseMapper.updateStatusBatch(status, ids);
        delShopAndCategoryAndTotal();
        return ResponseResult.okResult();
    }

    /**
     * 根据分类id查询商品
     *
     * @param id
     * @return
     */
    @Override
    public ResponseResult selectShopByCategoryId(Long id) {
        List<ShopExistTableVo> shopExistTableVos = baseMapper.selectShopByCategoryId(id, 0, 20);
        Integer count = baseMapper.selectMyCount(String.valueOf(id));
        ShopCategoryPageVo pageVo = new ShopCategoryPageVo(shopExistTableVos, count);
        return ResponseResult.okResult(pageVo);
    }


    @Override
    public ResponseResult updateShopById(ShopInfoVo shopInfoVo) {
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        Shop shop = BeanCopyUtils.copyBean(shopInfoVo, Shop.class);
        // 集合转字符串
        String images = shopInfoVo.getImages()
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        shop.setImage(images);
        Long categoryId = shopInfoVo.getCategoryId();
        try {
            // 修改商品表
            updateById(shop);
            // 修改分类商品表
            baseMapper.updateShopCategoryInfo(shop.getId(), categoryId);
            dataSourceTransactionManager.commit(transactionStatus);// 手动commit
            delShopAndCategoryAndTotal();
            return ResponseResult.okResult();
        } catch (Exception e) {
            dataSourceTransactionManager.rollback(transactionStatus);
            throw new RuntimeException("更新商品失败");
        }
    }

    @Override
    public ResponseResult getShopList() {
        LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Shop::getDelFlag, StatusEnum.SHOP_STATUS_NORMAL);// 0起售
        List<Shop> list = list(queryWrapper);
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

    /**
     * Page<Category> page = new Page<>(pageDto.getPageNum(), pageDto.getPageSize());
     * LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
     * // 排序
     * queryWrapper.orderByAsc(Category::getSort);
     * page(page, queryWrapper);
     * List<Category> records = page.getRecords();
     * List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(records, CategoryVo.class);
     * long total = page.getTotal();
     * CategoryPageVo pageVo = new CategoryPageVo(categoryVos, total);
     * return ResponseResult.okResult(pageVo);
     */
    @Override
    public ResponseResult getShopListByPageInfo(ShopPageInfoVo shopPageInfoVo) {
//        Page<Shop> page = new Page<>(pageDto.getPageNum(), pageDto.getPageSize());
        List<ShopExistTableVo> shopExistTableVos = null;
        ShopPageVo pageVo = null;
        Integer pageNum = (shopPageInfoVo.getPageNum() - 1) * shopPageInfoVo.getPageSize();
        // 总数据
        String total = "CategoryTotal:count_" + shopPageInfoVo.getPageNum() + "_" + shopPageInfoVo.getPageSize() + "_" + shopPageInfoVo.getCategorySelect();
        Integer count = null;
        count = (Integer) redisTemplate.opsForValue().get(total);
        if (count == null) {
            count = baseMapper.selectMyCount(shopPageInfoVo.getCategorySelect());
            redisTemplate.opsForValue().set(total, count, 1, TimeUnit.DAYS);
        }

        String shopPageInfo = "Shop:list_" + shopPageInfoVo.getPageNum() + "_" + shopPageInfoVo.getPageSize() + "_" + shopPageInfoVo.getCategorySelect();
        shopExistTableVos = (List<ShopExistTableVo>) redisTemplate.opsForValue().get(shopPageInfo);
        if (shopExistTableVos != null) {
            pageVo = new ShopPageVo(shopExistTableVos, count.longValue());
            return ResponseResult.okResult(pageVo);
        }

        shopExistTableVos = baseMapper.selectShopListAndCategoryName(pageNum, shopPageInfoVo.getPageSize(), shopPageInfoVo.getCategorySelect());
        shopExistTableVos.stream().map(item -> {
            String[] split = item.getImage().split(",");
            for (int i = 0; i < split.length; i++) {
                split[i] = BaseImageUrl + split[i];
            }
            String res = String.join(",", split);
            item.setImage(res);
            return item;
        }).collect(Collectors.toList());
        // 设置半个小时的缓存
        redisTemplate.opsForValue().set(shopPageInfo, shopExistTableVos, 1, TimeUnit.DAYS);
        pageVo = new ShopPageVo(shopExistTableVos, count.longValue());
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 前台筛选排序
     *
     * @param shopSortDto
     * @return
     */
    @Override
    public ResponseResult sortShop(ShopSortDto shopSortDto) {
        if (shopSortDto.getPageNum() == null) {
            shopSortDto.setPageNum(1);
        }
        if (shopSortDto.getPageSize() == null) {
            shopSortDto.setPageSize(20);
        }
        Integer pageNum = (shopSortDto.getPageNum() - 1) * shopSortDto.getPageSize();
        List<ShopExistTableVo> shopExistTableVos = baseMapper.sortShop(pageNum, shopSortDto.getPageSize(), shopSortDto);
        Integer size = shopExistTableVos.size();
        return ResponseResult.okResult(new PageVo(shopExistTableVos,size.longValue()));
    }

    @Override
    public ResponseResult queryShopById(Long id) {
        List<String> imagess = new ArrayList<>();
        Shop item = getById(id);

        Integer hasComment = baseMapper.hasComment(id);

        String[] split = item.getImage().split(",");
        for (String s : split) {
            s = BaseImageUrl + s;
            imagess.add(s);
        }
        ShopVo shopVo = BeanCopyUtils.copyBean(item, ShopVo.class);
        Float avg = 5.00F;
        // 如果有评论
        if (hasComment > 0) {
            // 需要查询该商品所有的评分平均
            avg = baseMapper.avgStar(id);
        }
        shopVo.setStar(avg);
        shopVo.setImages(imagess);
        return ResponseResult.okResult(shopVo);
    }

    @Override
    public ResponseResult searchByName(String name) {
        LambdaQueryWrapper<Shop> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Shop::getName, name);
        Page<Shop> page = new Page<>();
        page(page, queryWrapper);
        List<Shop> shopList = page.getRecords();
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
        return ResponseResult.okResult(new PageVo(shopExistTableVo, page.getTotal()));
    }

    private void delShopAndCategoryAndTotal() {
        String key1 = "Category";
        String key2 = "CategoryTotal";
        String[] keys = new String[]{
                "Category",
                "CategoryTotal",
                "Shop"
        };
        for (String key : keys) {
            delKey(key);
        }
    }

    private void delKey(String key) {
        log.info("Redis Key(key:{}) 开始删除", key);
        Collection<String> keys = redisCache.keys("*");
        keys.forEach(item -> {
            if (item.contains(key)) {
                redisCache.deleteObject(item);
            }
        });
        log.info("key:{} 删除成功", key);
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

