package com.hang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.dto.CategoryDelDto;
import com.hang.dto.CategoryDto;
import com.hang.dto.CategoryUpdateDto;
import com.hang.dto.PageDto;
import com.hang.entity.Category;
import com.hang.entity.Shop;
import com.hang.mapper.CategoryMapper;
import com.hang.result.ResponseResult;
import com.hang.service.CategoryService;
import com.hang.utils.BeanCopyUtils;
import com.hang.utils.RedisCache;
import com.hang.vo.CategoryPageVo;
import com.hang.vo.CategoryVo;
import com.hang.vo.ShopExistTableVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * (Category)表服务实现类
 *
 * @author makejava
 * @since 2023-08-15 21:09:28
 */
@Service("categoryService")
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisCache redisCache;
    @Override
    public ResponseResult toVoList() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        // 默认第一页 一页10个数据
        Page<Category> page = new Page<>(1, 10);
        page(page, queryWrapper);
        List<Category> records = page.getRecords();
        long total = page.getTotal();
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(records, CategoryVo.class);
        CategoryPageVo categoryPageVo = new CategoryPageVo(categoryVos, total);
        return ResponseResult.okResult(categoryPageVo);
    }

    /**
     * 更新分类信息
     *
     * @param categoryDto
     * @return
     */
    @Override
    public ResponseResult saveCategoryInfo(CategoryDto categoryDto) {
        Category category = BeanCopyUtils.copyBean(categoryDto, Category.class);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getId, category.getId());
        update(category, queryWrapper);
        delCategoryAndTotal();
        return ResponseResult.okResult();
    }

    /**
     * 根据名字搜索
     *
     * @param name
     * @return
     */
    @Override
    public ResponseResult searchCategory(String name) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Category::getName, name);
        List<Category> list = list(queryWrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult queryPage(PageDto pageDto) {
        List<Category> records = null;

        Page<Category> page = new Page<>(pageDto.getPageNum(), pageDto.getPageSize());
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 排序
        queryWrapper.orderByAsc(Category::getSort);
        page(page, queryWrapper);
        records = page.getRecords();
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(records, CategoryVo.class);
        long total = page.getTotal();
        CategoryPageVo pageVo = new CategoryPageVo(categoryVos, total);
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult changeCategoryStatusBatch(CategoryUpdateDto categoryUpdateDto) {
        Integer status = categoryUpdateDto.getStatus();
        List<Long> ids = categoryUpdateDto.getIds();
//        System.out.println(categoryUpdateDto);
        baseMapper.updateStatusBatch(status, ids);
        delCategoryAndTotal();
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult delBatchByIds(CategoryDelDto categoryDelDto) {
        List<Long> ids = categoryDelDto.getIds();
        int count = baseMapper.countIsToDel(ids);
        // 如果分类和商品表有关联数据,则不能删除
        if (count > 0) {
            return ResponseResult.errorResult(201, "当前选择的分类下关联了商品，不能删除");
        }
        // 如果没有 则可以删除
        baseMapper.deleteBatchIds(ids);
        delCategoryAndTotal();
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addCategory(CategoryDto categoryDto) {
        Category category = BeanCopyUtils.copyBean(categoryDto, Category.class);
        boolean flag = save(category);
        delCategoryAndTotal();
        return flag ? ResponseResult.okResult() : ResponseResult.errorResult(201, "新增失败");

    }

    @Override
    public ResponseResult listWithTotal() {
        CategoryPageVo pageVo = null;
        String categoryInfo = "Category:all_list";
        pageVo = redisCache.getCacheObject(categoryInfo);
        if(pageVo != null){
            return ResponseResult.okResult(pageVo);
        }
        List<Category> list = list(null);
        Integer total = list.size();
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);
        pageVo = new CategoryPageVo(categoryVos, total.longValue());
        redisCache.setCacheObject(categoryInfo,pageVo);
        redisCache.expire(categoryInfo,1, TimeUnit.DAYS);
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 根据shop的id 查询出每一个shop的分类
     *
     * @param shop
     * @return
     */
    @Override
    public List<ShopExistTableVo> getCategoryNameList(List<Shop> shop) {
        List<ShopExistTableVo> shopExistTableVos = BeanCopyUtils.copyBeanList(shop, ShopExistTableVo.class);
        shopExistTableVos.stream().map(item -> {
            item.setCategoryName(baseMapper.getCategoryNameByShopId(item.getId()));
            return item;
        }).collect(Collectors.toList());
        return shopExistTableVos;
    }

    /**
     * 根据名字获取id
     * @param name
     * @return
     */
    @Override
    public ResponseResult byNameFindCategoryId(String name) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getName,name);
        Category one = getOne(queryWrapper);
        // 现在返回的就是id
        return ResponseResult.okResult(one.getId());
    }

    @Override
    public ResponseResult byCategoryIdFindName(Long id) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getId,id);
        Category one = getOne(queryWrapper);
        return ResponseResult.okResult(one.getName());
    }

    /**
     * 删除分类列表和total
     */
    private void delCategoryAndTotal(){
        String key1 = "Category";
        String key2 = "CategoryTotal";
        delKey(key1);
        delKey(key2);
    }
    private void delKey(String key){
        log.info("Redis Key(key:{}) 开始删除",key);
        Collection<String> keys = redisCache.keys("*");
        keys.forEach(item->{
            if(item.contains(key)){
                redisCache.deleteObject(item);
            }
        });
        log.info("key:{} 删除成功",key);
    }
}

