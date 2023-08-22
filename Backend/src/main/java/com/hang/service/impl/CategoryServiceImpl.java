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
import com.hang.vo.CategoryPageVo;
import com.hang.vo.CategoryVo;
import com.hang.vo.ShopExistTableVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * (Category)表服务实现类
 *
 * @author makejava
 * @since 2023-08-15 21:09:28
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

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
        Page<Category> page = new Page<>(pageDto.getPageNum(), pageDto.getPageSize());
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 排序
        queryWrapper.orderByAsc(Category::getSort);
        page(page, queryWrapper);
        List<Category> records = page.getRecords();
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
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addCategory(CategoryDto categoryDto) {
        Category category = BeanCopyUtils.copyBean(categoryDto, Category.class);
        boolean flag = save(category);
        return flag ? ResponseResult.okResult() : ResponseResult.errorResult(201, "新增失败");

    }

    @Override
    public ResponseResult listWithTotal() {
        List<Category> list = list(null);
        Integer total = list.size();
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);
        CategoryPageVo pageVo = new CategoryPageVo(categoryVos, total.longValue());
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
}

