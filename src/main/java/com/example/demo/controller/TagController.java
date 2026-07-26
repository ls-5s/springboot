package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.common.Result;
import com.example.demo.model.entity.Tag;
import com.example.demo.repository.TagMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@io.swagger.v3.oas.annotations.tags.Tag(name = "标签接口")
@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagMapper tagMapper;

    // GET /api/tags — 标签列表
    @Operation(summary = "标签列表")
    @GetMapping
    public Result<List<Tag>> list() {
        List<Tag> list = tagMapper.selectList(
                new LambdaQueryWrapper<Tag>().orderByDesc(Tag::getCreateTime));
        return Result.success(list);
    }
}
