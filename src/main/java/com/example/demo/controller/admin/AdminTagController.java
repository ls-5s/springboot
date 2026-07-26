package com.example.demo.controller.admin;

import com.example.demo.common.Result;
import com.example.demo.model.entity.Tag;
import com.example.demo.repository.TagMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@io.swagger.v3.oas.annotations.tags.Tag(name = "管理后台-标签")
@RestController
@RequestMapping("/api/admin/tags")
@RequiredArgsConstructor
public class AdminTagController {

    private final TagMapper tagMapper;

    @Operation(summary = "新增标签")
    @PostMapping
    public Result<Void> create(@RequestBody Map<String, Object> body) {
        Tag tag = new Tag();
        tag.setName((String) body.get("name"));
        tagMapper.insert(tag);
        return Result.success();
    }

    @Operation(summary = "修改标签")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Tag tag = tagMapper.selectById(id);
        if (tag != null && body.containsKey("name")) {
            tag.setName(body.get("name"));
            tagMapper.updateById(tag);
        }
        return Result.success();
    }

    @Operation(summary = "删除标签")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        tagMapper.deleteById(id);
        return Result.success();
    }
}
