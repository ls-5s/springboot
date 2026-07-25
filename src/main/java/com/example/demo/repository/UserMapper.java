package com.example.demo.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

// 用户 Mapper：继承 BaseMapper 获得 CRUD 方法（selectById / insert / updateById / deleteById 等）
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
