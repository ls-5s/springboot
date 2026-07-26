package com.example.demo.controller.admin;

import com.example.demo.common.Result;
import com.example.demo.model.entity.SysConfig;
import com.example.demo.repository.SysConfigMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "管理后台-站点配置")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminSiteController {

    private final SysConfigMapper sysConfigMapper;

    // PUT /api/admin/site-config — 批量修改站点配置
    @Operation(summary = "修改站点配置")
    @PutMapping("/site-config")
    public Result<Void> updateConfig(@RequestBody Map<String, String> body) {
        for (Map.Entry<String, String> entry : body.entrySet()) {
            // 查已有配置，存在则更新，不存在则新增
            SysConfig config = sysConfigMapper.selectOne(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysConfig>()
                            .eq(SysConfig::getConfigKey, entry.getKey()));
            if (config != null) {
                config.setConfigValue(entry.getValue());
                sysConfigMapper.updateById(config);
            } else {
                config = new SysConfig();
                config.setConfigKey(entry.getKey());
                config.setConfigValue(entry.getValue());
                sysConfigMapper.insert(config);
            }
        }
        return Result.success();
    }
}
