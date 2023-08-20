package com.hang.controller;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hang.dto.PageDto;
import com.hang.dto.ShopDto;
import com.hang.dto.ShopInfoVo;
import com.hang.entity.Shop;
import com.hang.mapper.ShopMapper;
import com.hang.result.ResponseResult;
import com.hang.service.ShopService;
import com.hang.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.WebServlet;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private ShopMapper shopMapper;

    @PostMapping("/addShopInfo")
    public ResponseResult addShopInfo(@RequestBody ShopInfoVo shopInfoVo){
        return shopService.addShopInfo(shopInfoVo);
    }
    @GetMapping("/getShopList")
    public ResponseResult getShopList(){
        return shopService.getShopList();
    }

    /**
     * 后台商品分页接口
     * @param pageDto
     * @return
     */
    @PostMapping("/getShopListByPageInfo")
    public ResponseResult getShopListByPageInfo(@RequestBody PageDto pageDto){
        return shopService.getShopListByPageInfo(pageDto);
    }
    @GetMapping("/queryShopById")
    public ResponseResult queryShopById(@RequestParam("id") Long id){
        return shopService.queryShopById(id);
    }
    @GetMapping("/searchByName")
    public ResponseResult searchByName(@RequestParam("name") String name){
        return shopService.searchByName(name);
    }
    @GetMapping("getImgById")
    public ResponseResult getImgById(@RequestParam("id") Long id){
        return ResponseResult.okResult(shopMapper.getImgById(id));
    }
    @PostMapping("saveShop")
    public ResponseResult saveShop(@RequestBody ShopInfoVo shopInfoVo){
        return shopService.updateShopById(shopInfoVo);
    }
    @DeleteMapping
    public ResponseResult delShopBatchByIds(@RequestBody List<Long> ids){
        return shopService.delShopBatchByIds(ids);
    }
    @PostMapping("/changeShopStatusBatch")
    public ResponseResult changeShopStatusBatch(@RequestBody ShopDto shopDto){
        return shopService.changeShopStatusBatch(shopDto);
    }
    @GetMapping("/selectShopByCategory")
    public ResponseResult selectShopByCategory(@RequestParam("id") Long id){
        return shopService.selectShopByCategoryId(id);
    }
}

