package com.hang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.dto.CommentDto;
import com.hang.emuns.StatusEnum;
import com.hang.entity.Comment;
import com.hang.mapper.CommentMapper;
import com.hang.mapper.ShopMapper;
import com.hang.result.ResponseResult;
import com.hang.service.CommentService;
import com.hang.service.UserService;
import com.hang.utils.BeanCopyUtils;
import com.hang.utils.RedisCache;
import com.hang.vo.CommentVo;
import com.hang.vo.PageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * (Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-08-24 17:02:26
 */
@Service("commentService")
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    private final static String BaseImageUrl = "http://rzl9bicnx.hn-bkt.clouddn.com/";
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult queryCommentList(Integer pageNum, Integer pageSize, Long shopId) {
        List<CommentVo> commentVoList = null;
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        // 查询商品
        queryWrapper.eq(Comment::getShopId, shopId)
                // 查询商品的根评论 rootId为-1
                .eq(Comment::getRootId, StatusEnum.ROOT_COMMENT);
        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        String shopCommentList = "Comment:ShopCommentList_" + shopId + "_" + pageNum + "_" + pageSize;
        commentVoList = (List<CommentVo>) redisTemplate.opsForValue().get(shopCommentList);
        if (commentVoList != null) {
            return ResponseResult.okResult(new PageVo(commentVoList, page.getTotal()));
        }
        commentVoList = toCommentVoList(page.getRecords());
        // 查询根评论对应的子评论集合,赋值给对应的属性(children)
        for (CommentVo vo : commentVoList) {
            // 处理照片
            String images = vo.getImages();
            List<String> ss = new ArrayList<>();
            if (images != null) {
                String[] split = images.split(",");
                for (String s : split) {
                    s = BaseImageUrl + s;
                    ss.add(s);
                }
                // 设置前缀 父级
                vo.setImagess(ss);
            }
            Long id = vo.getId();
            // 如果有评论,则取平均
            Integer integer = shopMapper.hasComment(id);
            if (integer > 0) {
                vo.setStar(shopMapper.avgStar(id));
            }
            List<CommentVo> children = getChildren(id);
            vo.setChildren(children);
        }
        redisTemplate.opsForValue().set(shopCommentList, commentVoList, 1, TimeUnit.DAYS);
        return ResponseResult.okResult(new PageVo(commentVoList, page.getTotal()));
    }

    @Override
    public ResponseResult addComment(CommentDto commentDto) {
        String content = commentDto.getContent();
        CommentVo commentInfo = commentDto.getCommentInfo();
        Boolean flag = baseMapper.addComment(content,commentInfo);
        if(flag){
            delKey("Comment");
        }

        return flag ? ResponseResult.okResult() : ResponseResult.errorResult(202,"回复失败");
    }

    /**
     * comment -> commentVo
     *
     * @param commentList
     * @return
     */
    private List<CommentVo> toCommentVoList(List<Comment> commentList) {
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(commentList, CommentVo.class);
        // 还有需要去处理的值，遍历vo
        // 通过creatBy查询用户昵称并赋值
        for (CommentVo vo : commentVos) {
            // getUid对应的用户id
            String nikeName = userService.getById(vo.getUid()).getNickName();
            vo.setNikeName(nikeName);
            // 通过toCommentUserId查询用户昵称并赋值
            // 如果toCommentId不为-1 才进行查询
            if (vo.getUid() != -1) {
                String toCommentUserName = userService.getById(vo.getUid()).getNickName();
                vo.setNikeName(toCommentUserName);
            }
        }
        return commentVos;
    }

    /**
     * 查询根评论下面的子评论
     *
     * @param rootId 根据 根id
     * @return
     */
    private List<CommentVo> getChildren(Long rootId) {
        // 如果评论过多需要考虑分页,现在不考虑
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId, rootId)
                .orderByAsc(Comment::getCreateTime);
        List<Comment> list = list(queryWrapper);
        // 直接调用之前封装的方法
        List<CommentVo> commentVos = toCommentVoList(list);

        commentVos.forEach(item -> {
            List<String> imagess = new ArrayList<>();
            String images = item.getImages();
            // 如果照片不为空
            if (images != null) {
                String[] split = images.split(",");
                for (String s : split) {
                    s = BaseImageUrl + s;
                    imagess.add(s);
                }
            }
            item.setImagess(imagess);
        });
        return commentVos;
    }
    private void delKey(String key) {
        log.info("Redis Key(key:{}) 开始删除", key);
        Collection<String> keys = redisCache.keys("*");
        keys.forEach(item -> {
            if (item.contains(key)) {
                redisCache.deleteObject(item);
            }
        });
        log.info("key:{} 删除成功", key);
    }
}

