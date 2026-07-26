package com.example.demo.controller.admin;

import com.example.demo.common.Result;
import com.example.demo.model.entity.FriendLink;
import com.example.demo.repository.FriendLinkMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "管理后台-友链")
@RestController
@RequestMapping("/api/admin/friend-links")
@RequiredArgsConstructor
public class AdminFriendLinkController {

    private final FriendLinkMapper friendLinkMapper;

    @Operation(summary = "新增友链")
    @PostMapping
    public Result<Void> create(@RequestBody Map<String, Object> body) {
        FriendLink link = new FriendLink();
        link.setName((String) body.get("name"));
        link.setUrl((String) body.get("url"));
        link.setLogo((String) body.getOrDefault("logo", null));
        link.setSort(body.get("sort") != null ? ((Number) body.get("sort")).intValue() : 0);
        friendLinkMapper.insert(link);
        return Result.success();
    }

    @Operation(summary = "修改友链")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        FriendLink link = friendLinkMapper.selectById(id);
        if (link != null) {
            if (body.containsKey("name")) link.setName((String) body.get("name"));
            if (body.containsKey("url")) link.setUrl((String) body.get("url"));
            if (body.containsKey("logo")) link.setLogo((String) body.get("logo"));
            if (body.containsKey("sort")) link.setSort(((Number) body.get("sort")).intValue());
            friendLinkMapper.updateById(link);
        }
        return Result.success();
    }

    @Operation(summary = "删除友链")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        friendLinkMapper.deleteById(id);
        return Result.success();
    }
}
