package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.dto.CommentVO;
import com.example.demo.model.entity.Comment;

import java.util.List;

public interface CommentService extends IService<Comment> {

    // 文章评论列表（含二级回复，按时间排序）
    List<CommentVO> getCommentsByArticle(Long articleId);
}
