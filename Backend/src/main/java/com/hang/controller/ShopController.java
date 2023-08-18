package com.hang.controller;



import com.hang.dto.ShopInfoVo;
import com.hang.entity.Shop;
import com.hang.result.ResponseResult;
import com.hang.service.ShopService;
import com.hang.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * (Shop)表控制层
 *
 * @author makejava
 * @since 2023-08-17 19:27:37
 */
@RestController
@RequestMapping("/shop")
public class ShopController{
    @Autowired
    private ShopService shopService;

    @PostMapping("/addShopInfo")
    public ResponseResult addShopInfo(@RequestBody ShopInfoVo shopInfoVo){

        return shopService.addShopInfo(shopInfoVo);
    }
}

