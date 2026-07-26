package com.example.demo.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

// 系统配置实体，对应 sys_config 表
@Data
@TableName("sys_config")
public class SysConfig {

    @TableId(type = IdType.AUTO) // 主键自增
    private Long id;

    private String configKey;    // 配置键

    private String configValue;  // 配置值
}
