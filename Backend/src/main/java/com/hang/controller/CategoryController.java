package com.hang.controller;



import com.hang.dto.CategoryDelDto;
import com.hang.dto.CategoryDto;
import com.hang.dto.CategoryUpdateDto;
import com.hang.dto.PageDto;
import com.hang.result.ResponseResult;
import com.hang.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * (Category)表控制层
 *
 * @author makejava
 * @since 2023-08-15 21:09:28
 */
@RestController
@RequestMapping("/category")
public class CategoryController{
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/getCategoryList")
    public ResponseResult getCategoryList(){
        return categoryService.toVoList();

    }
    @PostMapping("/updateCategoryInfo")
    public ResponseResult updateCategoryInfo(@RequestBody CategoryDto categoryDto){
        return categoryService.saveCategoryInfo(categoryDto);
    }
    @GetMapping("/searchCategory")
    public ResponseResult searchCategory(@RequestParam("name") String name){
        return categoryService.searchCategory(name);
    }
    @PostMapping("/queryPage")
    public ResponseResult queryPage(@RequestBody PageDto pageDto){
        return categoryService.queryPage(pageDto);
    }
    @PostMapping("/changeCategoryStatusBatch")
    public ResponseResult changeCategoryStatusBatch(@RequestBody CategoryUpdateDto categoryUpdateDto){
        return categoryService.changeCategoryStatusBatch(categoryUpdateDto);
    }
    @DeleteMapping
    public ResponseResult delBatchByIds(@RequestBody CategoryDelDto categoryDelDto){
        return categoryService.delBatchByIds(categoryDelDto);
    }
}

