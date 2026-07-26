package com.example.demo.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

// 分类实体，对应 category 表
@Data
@TableName("category")
public class Category {

    @TableId(type = IdType.AUTO) // 主键自增
    private Long id;

    private String name;     // 分类名称

    private Integer sort;    // 排序，越小越靠前

    @TableField(fill = FieldFill.INSERT) // 插入时自动填充
    private LocalDateTime createTime;
}
