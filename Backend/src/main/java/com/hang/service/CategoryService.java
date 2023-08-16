package com.hang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hang.dto.CategoryDto;
import com.hang.dto.CategoryUpdateDto;
import com.hang.dto.PageDto;
import com.hang.entity.Category;
import com.hang.result.ResponseResult;

import java.util.List;


/**
 * (Category)表服务接口
 *
 * @author makejava
 * @since 2023-08-15 21:09:28
 */
public interface CategoryService extends IService<Category> {

    ResponseResult toVoList();

    ResponseResult saveCategoryInfo(CategoryDto categoryDto);

    ResponseResult searchCategory(String name);

    ResponseResult queryPage(PageDto pageDto);

    ResponseResult changeCategoryStatusBatch(CategoryUpdateDto categoryUpdateDto);
}

