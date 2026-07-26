package com.example.demo.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

// 友链实体，对应 friend_link 表
@Data
@TableName("friend_link")
public class FriendLink {

    @TableId(type = IdType.AUTO) // 主键自增
    private Long id;

    private String name;     // 网站名称

    private String url;      // 网站地址

    private String logo;     // Logo URL

    private Integer sort;    // 排序
}
