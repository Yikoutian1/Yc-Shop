package com.hang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hang.dto.*;
import com.hang.entity.Shop;
import com.hang.result.ResponseResult;

import java.util.List;


/**
 * (Shop)表服务接口
 *
 * @author makejava
 * @since 2023-08-17 19:27:37
 */
public interface ShopService extends IService<Shop> {

    ResponseResult addShopInfo(ShopInfoVo shopInfoVo);

    ResponseResult getShopList();

    ResponseResult getShopListByPageInfo(ShopPageInfoVo shopPageInfoVo);

    ResponseResult queryShopById(Long id);

    ResponseResult searchByName(String name);

    ResponseResult updateShopById(ShopInfoVo shopInfoVo);

    ResponseResult delShopBatchByIds(List<Long> ids);

    ResponseResult changeShopStatusBatch(ShopDto shopDto);

    ResponseResult selectShopByCategoryId(Long id);

    ResponseResult sortShop(ShopSortDto shopSortDto);
}

