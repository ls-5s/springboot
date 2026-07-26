package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.common.Result;
import com.example.demo.model.entity.FriendLink;
import com.example.demo.repository.FriendLinkMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "友链接口")
@RestController
@RequestMapping("/api/friend-links")
@RequiredArgsConstructor
public class FriendLinkController {

    private final FriendLinkMapper friendLinkMapper;

    // GET /api/friend-links — 友链列表（按 sort 排序）
    @Operation(summary = "友链列表")
    @GetMapping
    public Result<List<FriendLink>> list() {
        List<FriendLink> list = friendLinkMapper.selectList(
                new LambdaQueryWrapper<FriendLink>().orderByAsc(FriendLink::getSort));
        return Result.success(list);
    }
}
