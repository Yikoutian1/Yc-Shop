package com.hang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hang.dto.ShopInfoVo;
import com.hang.entity.Shop;
import com.hang.vo.ShopExistTableVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * (Shop)表数据库访问层
 *
 * @author makejava
 * @since 2023-08-17 19:27:37
 */
@Mapper
public interface ShopMapper extends BaseMapper<Shop> {

    void insertIntoShopCategory(@Param("vo") ShopInfoVo shopInfoVo,@Param("shop_id") Long id);
    String getImgById(@Param("id")Long id);

    List<ShopExistTableVo> selectShopListAndCategoryName(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);

    void updateStatusBatch(@Param("status") Integer status,@Param("ids") List<Long> ids);

    List<ShopExistTableVo> selectShopByCategoryId(@Param("id") Long id);

    void updateShopCategoryInfo(@Param("shop_id") Long id,@Param("category_id") Long categoryId);
}

