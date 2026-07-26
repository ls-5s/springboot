package com.example.demo.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.common.Result;
import com.example.demo.model.entity.Comment;
import com.example.demo.model.entity.User;
import com.example.demo.repository.CommentMapper;
import com.example.demo.repository.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name = "管理后台-评论")
@RestController
@RequestMapping("/api/admin/comments")
@RequiredArgsConstructor
public class AdminCommentController {

    private final CommentMapper commentMapper;
    private final UserMapper userMapper;

    // GET /api/admin/comments/pending — 待审核评论
    @Operation(summary = "待审核评论")
    @GetMapping("/pending")
    public Result<List<Map<String, Object>>> pending() {
        List<Comment> list = commentMapper.selectList(
                new LambdaQueryWrapper<Comment>().eq(Comment::getStatus, 0).orderByDesc(Comment::getCreateTime));
        List<Map<String, Object>> result = new ArrayList<>();
        for (Comment c : list) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", c.getId());
            map.put("articleId", c.getArticleId());
            map.put("userId", c.getUserId());
            map.put("content", c.getContent());
            map.put("status", c.getStatus());
            map.put("createTime", c.getCreateTime());
            String userName = c.getVisitorName();
            if (c.getUserId() != null) {
                User u = userMapper.selectById(c.getUserId());
                if (u != null) userName = u.getNickname() != null ? u.getNickname() : u.getUsername();
            }
            map.put("userName", userName);
            result.add(map);
        }
        return Result.success(result);
    }

    // PUT /api/admin/comments/{id}/approve — 审核通过
    @Operation(summary = "审核通过")
    @PutMapping("/{id}/approve")
    public Result<Void> approve(@PathVariable Long id) {
        Comment comment = commentMapper.selectById(id);
        if (comment != null) {
            comment.setStatus(1);
            commentMapper.updateById(comment);
        }
        return Result.success();
    }

    // DELETE /api/admin/comments/{id} — 删除评论
    @Operation(summary = "删除评论")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        commentMapper.deleteById(id);
        return Result.success();
    }
}
