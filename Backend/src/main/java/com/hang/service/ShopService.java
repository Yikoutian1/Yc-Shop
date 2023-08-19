package com.hang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hang.dto.PageDto;
import com.hang.dto.ShopInfoVo;
import com.hang.entity.Shop;
import com.hang.result.ResponseResult;


/**
 * (Shop)表服务接口
 *
 * @author makejava
 * @since 2023-08-17 19:27:37
 */
public interface ShopService extends IService<Shop> {

    ResponseResult addShopInfo(ShopInfoVo shopInfoVo);

    ResponseResult getShopList();

    ResponseResult getShopListByPageInfo(PageDto pageDto);

    ResponseResult queryShopById(Long id);

    ResponseResult searchByName(String name);

    ResponseResult updateShopById(ShopInfoVo shopInfoVo);
}

