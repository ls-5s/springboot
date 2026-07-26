package com.example.demo.controller.admin;

import com.example.demo.common.Result;
import com.example.demo.model.entity.Category;
import com.example.demo.repository.CategoryMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "管理后台-分类")
@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryMapper categoryMapper;

    // POST /api/admin/categories — 新增分类
    @Operation(summary = "新增分类")
    @PostMapping
    public Result<Void> create(@RequestBody Map<String, Object> body) {
        Category category = new Category();
        category.setName((String) body.get("name"));
        category.setSort(body.get("sort") != null ? ((Number) body.get("sort")).intValue() : 0);
        categoryMapper.insert(category);
        return Result.success();
    }

    // PUT /api/admin/categories/{id} — 修改分类
    @Operation(summary = "修改分类")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Category category = categoryMapper.selectById(id);
        if (category != null) {
            if (body.containsKey("name")) category.setName((String) body.get("name"));
            if (body.containsKey("sort")) category.setSort(((Number) body.get("sort")).intValue());
            categoryMapper.updateById(category);
        }
        return Result.success();
    }

    // DELETE /api/admin/categories/{id} — 删除分类
    @Operation(summary = "删除分类")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        categoryMapper.deleteById(id);
        return Result.success();
    }
}
