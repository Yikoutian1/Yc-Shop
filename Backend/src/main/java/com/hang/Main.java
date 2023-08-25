package com.hang;

import com.hang.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Set;

/**
 * @ClassName Main
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/8/24 21:05
 * @Version 1.0
 */
@SpringBootTest
@Slf4j
public class Main {
    @Autowired
    private RedisCache redisCache;

    @Test
    public void testRemoveRedisKey(){
        String key = "shop";
        Collection<String> keys = redisCache.keys("*");
        keys.forEach(item->{
            if(item.contains(key)){
                redisCache.deleteObject(item);
            }
        });
        log.info("模糊匹配删除成功!{}",key);
//        redisCache.deleteObject(key);
    }
}
