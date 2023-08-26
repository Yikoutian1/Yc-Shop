package com.hang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hang.emuns.StatusEnum;
import com.hang.entity.Comment;
import com.hang.mapper.CommentMapper;
import com.hang.mapper.ShopMapper;
import com.hang.result.ResponseResult;
import com.hang.service.CommentService;
import com.hang.service.UserService;
import com.hang.utils.BeanCopyUtils;
import com.hang.vo.CommentVo;
import com.hang.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * (Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-08-24 17:02:26
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    private final static String BaseImageUrl = "http://rzl9bicnx.hn-bkt.clouddn.com/";
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ShopMapper shopMapper;

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
            String[] split = images.split(",");
            for (int i = 0; i < split.length; i++) {
                split[i] = BaseImageUrl + split[i];
            }
            String res = String.join(",", split);
            // 设置前缀 父级
            vo.setImages(res);

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
        list.stream().map(item->{
            // 处理照片
            String[] split = item.getImages().split(",");
            for (int i = 0; i < split.length; i++) {
                split[i] = BaseImageUrl + split[i];
            }
            String res = String.join(",", split);
            // 设置前缀 子级
            item.setImages(res);
            return item;
        }).collect(Collectors.toList());
        // 直接调用之前封装的方法
        List<CommentVo> commentVos = toCommentVoList(list);
        return commentVos;
    }
}

