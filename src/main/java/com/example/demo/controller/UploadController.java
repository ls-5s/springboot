package com.example.demo.controller;

import com.example.demo.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Tag(name = "文件上传")
@Slf4j
@RestController
@RequestMapping("/api/upload")
public class UploadController {

    // 上传目录，默认项目根目录下的 uploads/
    @Value("${upload.path:uploads}")
    private String uploadPath;

    @Operation(summary = "上传图片")
    @PostMapping
    public Result<Map<String, String>> upload(@RequestParam("file") MultipartFile file) throws IOException {
        // 校验类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return Result.fail("只支持上传图片");
        }
        // 校验大小（最大 5MB）
        if (file.getSize() > 5 * 1024 * 1024) {
            return Result.fail("文件不能超过 5MB");
        }

        // 生成文件名
        String originalName = file.getOriginalFilename();
        String ext = "";
        if (originalName != null && originalName.contains(".")) {
            ext = originalName.substring(originalName.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID() + ext;

        // 创建目录
        Path dir = Paths.get(uploadPath);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }

        // 保存文件
        File dest = dir.resolve(fileName).toFile();
        file.transferTo(dest);
        log.info("文件上传成功: {}", dest.getAbsolutePath());

        String url = "/uploads/" + fileName;
        Map<String, String> data = new LinkedHashMap<>();
        data.put("url", url);
        data.put("name", originalName);
        return Result.success(data);
    }
}
