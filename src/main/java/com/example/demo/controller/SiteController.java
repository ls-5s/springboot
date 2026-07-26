package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.common.Result;
import com.example.demo.model.entity.SysConfig;
import com.example.demo.repository.SysConfigMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "站点接口")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SiteController {

    private final SysConfigMapper sysConfigMapper;

    // GET /api/site-info — 站点信息，返回所有配置的 key-value
    @Operation(summary = "站点信息")
    @GetMapping("/site-info")
    public Result<Map<String, String>> siteInfo() {
        List<SysConfig> list = sysConfigMapper.selectList(new LambdaQueryWrapper<>());
        Map<String, String> map = new LinkedHashMap<>();
        for (SysConfig config : list) {
            map.put(config.getConfigKey(), config.getConfigValue());
        }
        return Result.success(map);
    }
}
