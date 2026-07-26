package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dto.CommentDTO;
import com.example.demo.dto.CommentVO;
import com.example.demo.model.entity.Comment;
import com.example.demo.model.entity.User;
import com.example.demo.repository.CommentMapper;
import com.example.demo.repository.UserMapper;
import com.example.demo.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private final UserMapper userMapper;

    public CommentServiceImpl(CommentMapper commentMapper, UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public List<CommentVO> getCommentsByArticle(Long articleId) {
        // 查该文章所有已通过的评论，按时间排序
        List<Comment> all = list(new LambdaQueryWrapper<Comment>()
                .eq(Comment::getArticleId, articleId)
                .eq(Comment::getStatus, 1)
                .orderByAsc(Comment::getCreateTime));

        if (all.isEmpty()) return Collections.emptyList();

        // 收集用户 ID，批量查询
        Set<Long> userIds = all.stream()
                .map(Comment::getUserId).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<Long, User> userMap = Collections.emptyMap();
        if (!userIds.isEmpty()) {
            userMap = userMapper.selectBatchIds(userIds).stream()
                    .collect(Collectors.toMap(User::getId, u -> u));
        }

        // 转 VO
        final Map<Long, User> finalUserMap = userMap;
        List<CommentVO> allVos = all.stream().map(c -> {
            String avatar = null;
            User u = c.getUserId() != null ? finalUserMap.get(c.getUserId()) : null;
            String userName;
            if (u != null) {
                userName = u.getNickname() != null ? u.getNickname() : u.getUsername();
                avatar = u.getAvatar();
            } else {
                userName = c.getVisitorName();
            }
            return CommentVO.builder()
                    .id(c.getId())
                    .userId(c.getUserId())
                    .userName(userName)
                    .avatar(avatar)
                    .content(c.getContent())
                    .createTime(c.getCreateTime())
                    .children(new ArrayList<>())
                    .build();
        }).collect(Collectors.toList());

        // 按 parentId 分组，parentId=null 为一级评论
        Map<Long, List<CommentVO>> grouped = allVos.stream()
                .filter(v -> all.stream().anyMatch(c ->
                        c.getId().equals(v.getId()) && c.getParentId() != null))
                .collect(Collectors.groupingBy(v -> {
                    Comment c = all.stream().filter(x -> x.getId().equals(v.getId())).findFirst().orElse(null);
                    return c != null ? c.getParentId() : 0L;
                }));

        // 简化：遍历挂载子回复
        Map<Long, CommentVO> voMap = allVos.stream().collect(Collectors.toMap(CommentVO::getId, v -> v, (a, b) -> a, LinkedHashMap::new));
        for (Comment c : all) {
            if (c.getParentId() != null) {
                CommentVO parent = voMap.get(c.getParentId());
                CommentVO child = voMap.get(c.getId());
                if (parent != null && child != null) {
                    parent.getChildren().add(child);
                }
            }
        }

        // 只返回一级评论
        return allVos.stream()
                .filter(v -> all.stream().anyMatch(c ->
                        c.getId().equals(v.getId()) && c.getParentId() == null))
                .collect(Collectors.toList());
    }

    // 发表评论：登录用户或游客
    @Override
    public void createComment(Long userId, CommentDTO dto) {
        Comment comment = new Comment();
        comment.setArticleId(dto.getArticleId());
        comment.setParentId(dto.getParentId());
        comment.setContent(dto.getContent());
        comment.setUserId(userId);
        comment.setVisitorName(userId == null ? dto.getVisitorName() : null);
        comment.setStatus(0); // 默认待审核
        save(comment);
    }
}
