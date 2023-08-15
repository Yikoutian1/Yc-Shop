package com.hang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.entity.Category;
import com.hang.mapper.CategoryMapper;
import com.hang.result.ResponseResult;
import com.hang.service.CategoryService;
import com.hang.utils.BeanCopyUtils;
import com.hang.vo.CategoryVo;
import org.springframework.stereotype.Service;

import java.util.List;

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
        List<Category> list = list(queryWrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }
}

