package com.example.demo.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

// 标签实体，对应 tag 表
@Data
@TableName("tag")
public class Tag {

    @TableId(type = IdType.AUTO) // 主键自增
    private Long id;

    private String name;     // 标签名称

    @TableField(fill = FieldFill.INSERT) // 插入时自动填充
    private LocalDateTime createTime;
}
