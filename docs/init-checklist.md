# 项目初始化清单

## 一、依赖清单 (pom.xml)

| 依赖 | 版本 | 作用 |
|------|------|------|
| spring-boot-starter-webmvc | 4.1.0 | Web 框架，Controller + Tomcat |
| spring-boot-starter-validation | 4.1.0 | 参数校验 @Valid |
| mybatis-plus-spring-boot4-starter | 3.5.15 | ORM，BaseMapper 自带 CRUD |
| mybatis-plus-jsqlparser | 3.5.15 | 分页插件 SQL 解析 |
| mysql-connector-j | 9.3.0 | MySQL 驱动 |
| druid-spring-boot-4-starter | 1.2.28 | 连接池 + SQL 监控面板 |
| spring-boot-starter-data-redis | 4.1.0 | Redis 缓存 |
| springdoc-openapi-starter-webmvc-ui | 3.0.3 | 接口文档 Swagger UI |
| lombok | 1.18.46 | 简化代码 @Data @Slf4j |
| spring-boot-devtools | 4.1.0 | 热更新 |
| hutool-all | 5.8.37 | 通用工具类 |
| spring-boot-starter-test | 4.1.0 | JUnit 5 + Mockito |

## 二、配置文件 (resources)

| 文件 | 内容 |
|------|------|
| application.yml | 应用名 + 激活 dev + Jackson 日期格式 `yyyy-MM-dd HH:mm:ss` |
| application-dev.yml | 数据库 root/123456 + Druid 监控 + Redis + MyBatis SQL 打印 + debug 日志 + 热更新 + 接口文档开启 |
| application-prod.yml | 环境变量注入敏感信息 + 接口文档关闭 |
| logback-spring.xml | 控制台彩色日志 + 文件滚动留存 30 天（单文件 100MB） |
| .gitignore | 排除 target / .idea / logs / .env |

## 三、公共模块 (common)

| 类 | 作用 |
|------|------|
| Result\<T\> | 统一响应体 `{code, message, data}`，静态方法 success() / fail() |
| ResultCode | 状态码枚举 200/400/401/403/404/500/510 |
| BusinessException | 自定义业务异常，Service 层 throw，自动转 Result |
| GlobalExceptionHandler | @RestControllerAdvice 全局拦截：业务异常 → 参数校验 → 兜底异常 |

## 四、配置类 (config)

| 类 | 作用 |
|------|------|
| MyBatisPlusConfig | @MapperScan 扫描 repository + MySQL 分页插件 |
| MyBatisPlusMetaHandler | 自动填充 createTime / updateTime |
| CorsConfig | 全局跨域，允许所有来源/方法/请求头 |
| RedisConfig | RedisTemplate 序列化，Key→String，Value→JSON |
| Knife4jConfig | 接口文档标题/版本/描述 |

## 五、目录结构

| 目录 | 用途 |
|------|------|
| controller/ | 接收请求，调 Service，返回 Result |
| service/ | 业务接口定义 |
| service/impl/ | 业务逻辑实现 |
| repository/ | MyBatis-Plus Mapper 接口 |
| model/entity/ | 数据库实体类 |
| dto/ | 请求/响应数据传输对象 |
| config/ | Spring Bean 配置 |
| common/ | 通用响应 + 异常处理 |
| util/ | 项目工具类 |
| resources/mapper/ | MyBatis XML 映射文件 |
| resources/db/migration/ | 数据库版本迁移脚本 |
| resources/static/ | 静态资源 |
| resources/templates/ | 页面模板 |

## 六、可访问端点

| 地址 | 说明 |
|------|------|
| http://localhost:8080/api/test/hello | 测试接口 |
| http://localhost:8080/swagger-ui.html | 接口文档 |
| http://localhost:8080/druid | Druid 监控 (admin/admin) |
