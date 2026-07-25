package com.example.demo.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

// 用户实体，对应 user 表
@Data
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO) // 主键自增
    private Long id;

    private String username; // 用户名，唯一

    private String password; // BCrypt 加密后的密码

    private String nickname; // 昵称

    private String email;    // 邮箱

    private String avatar;   // 头像路径

    private Integer status;  // 0=禁用 1=正常

    @TableField(fill = FieldFill.INSERT)        // 插入时自动填充
    private LocalDateTime createTime;            // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)  // 插入和更新时自动填充
    private LocalDateTime updateTime;            // 更新时间

    @TableLogic // 逻辑删除：deleteById 变为 update deleted=1
    private Integer deleted;                     // 0=正常 1=已删除
}
