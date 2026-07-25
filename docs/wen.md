# 个人博客后端设计

## 1. 技术栈

| 组件 | 选型 |
|------|------|
| 框架 | Spring Boot 4.1.0 |
| ORM | MyBatis-Plus 3.5.15 |
| 数据库 | MySQL 8.0 |
| 连接池 | Druid 1.2.28 |
| 缓存 | Redis |
| 认证 | Spring Security + JWT |
| 文档 | SpringDoc 3.0.3 |
| 工具 | Lombok / Hutool |

## 2. 数据库设计

### 2.1 用户表 (user)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键，自增 |
| username | VARCHAR(50) | 用户名，唯一 |
| password | VARCHAR(255) | BCrypt 加密 |
| nickname | VARCHAR(50) | 昵称 |
| email | VARCHAR(100) | 邮箱 |
| avatar | VARCHAR(255) | 头像 URL |
| status | TINYINT | 0禁用 1正常 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |
| deleted | TINYINT | 逻辑删除 0正常 1删除 |

### 2.2 分类表 (category)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键，自增 |
| name | VARCHAR(50) | 分类名称 |
| sort | INT | 排序，越小越靠前 |
| create_time | DATETIME | 创建时间 |

### 2.3 标签表 (tag)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键，自增 |
| name | VARCHAR(50) | 标签名称 |
| create_time | DATETIME | 创建时间 |

### 2.4 文章表 (article)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键，自增 |
| title | VARCHAR(100) | 标题 |
| summary | VARCHAR(500) | 摘要 |
| content | LONGTEXT | 正文 (Markdown) |
| cover | VARCHAR(255) | 封面图 URL |
| category_id | BIGINT | 分类 ID |
| user_id | BIGINT | 作者 ID |
| view_count | INT | 浏览量 |
| like_count | INT | 点赞数 |
| comment_count | INT | 评论数 |
| status | TINYINT | 0草稿 1发布 2私密 |
| is_top | TINYINT | 是否置顶 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |
| deleted | TINYINT | 逻辑删除 |

### 2.5 文章-标签关联表 (article_tag)

| 字段 | 类型 | 说明 |
|------|------|------|
| article_id | BIGINT | 文章 ID |
| tag_id | BIGINT | 标签 ID |

### 2.6 评论表 (comment)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键，自增 |
| article_id | BIGINT | 文章 ID |
| user_id | BIGINT | 评论人 ID (登录用户) |
| parent_id | BIGINT | 父评论 ID，为空则为一级评论 |
| visitor_name | VARCHAR(50) | 游客昵称 (未登录) |
| content | TEXT | 评论内容 |
| status | TINYINT | 0待审 1通过 2拒绝 |
| create_time | DATETIME | 创建时间 |

### 2.7 友链表 (friend_link)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键，自增 |
| name | VARCHAR(50) | 网站名称 |
| url | VARCHAR(255) | 网站地址 |
| logo | VARCHAR(255) | Logo URL |
| sort | INT | 排序 |

### 2.8 系统配置表 (sys_config)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| config_key | VARCHAR(50) | 配置键 |
| config_value | TEXT | 配置值 |

## 3. API 设计

### 3.1 公开接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/articles` | 文章列表（分页） |
| GET | `/api/articles/{id}` | 文章详情 |
| GET | `/api/articles/archive` | 文章归档（按年月） |
| GET | `/api/categories` | 分类列表 |
| GET | `/api/tags` | 标签列表 |
| GET | `/api/comments/article/{id}` | 某篇文章的评论 |
| GET | `/api/friend-links` | 友链列表 |
| POST | `/api/auth/register` | 注册 |
| POST | `/api/auth/login` | 登录 |
| GET | `/api/site-info` | 站点信息（标题、关于我、社交链接等） |

### 3.2 认证接口（需要登录）

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/user/info` | 当前用户信息 |
| PUT | `/api/user/info` | 修改个人信息 |
| POST | `/api/articles` | 发布文章 |
| PUT | `/api/articles/{id}` | 修改文章 |
| DELETE | `/api/articles/{id}` | 删除文章 |
| POST | `/api/comments` | 发表评论 |
| POST | `/api/upload` | 上传文件（图片） |

### 3.3 管理后台接口（admin 角色）

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/admin/articles` | 文章管理列表（含草稿） |
| POST | `/api/admin/categories` | 新增分类 |
| PUT | `/api/admin/categories/{id}` | 修改分类 |
| DELETE | `/api/admin/categories/{id}` | 删除分类 |
| POST | `/api/admin/tags` | 新增标签 |
| PUT | `/api/admin/tags/{id}` | 修改标签 |
| DELETE | `/api/admin/tags/{id}` | 删除标签 |
| GET | `/api/admin/comments/pending` | 待审核评论 |
| PUT | `/api/admin/comments/{id}/approve` | 审核通过 |
| DELETE | `/api/admin/comments/{id}` | 删除评论 |
| POST | `/api/admin/friend-links` | 新增友链 |
| PUT | `/api/admin/friend-links/{id}` | 修改友链 |
| DELETE | `/api/admin/friend-links/{id}` | 删除友链 |
| PUT | `/api/admin/site-config` | 修改站点配置 |

### 3.4 统一返回格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

### 3.5 分页请求/响应

请求：`/api/articles?page=1&size=10&categoryId=1&tagId=2&keyword=spring`

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "records": [],
    "total": 100,
    "size": 10,
    "current": 1,
    "pages": 10
  }
}
```

## 4. 项目结构

```
com.example.demo
├── common/
│   ├── Result.java                  # 统一响应
│   ├── ResultCode.java              # 状态码枚举
│   └── exception/
│       ├── BusinessException.java   # 业务异常
│       └── GlobalExceptionHandler   # 全局异常处理
├── config/
│   ├── MyBatisPlusConfig.java       # 分页插件
│   ├── CorsConfig.java              # 跨域
│   ├── RedisConfig.java             # Redis 序列化
│   ├── Knife4jConfig.java           # 接口文档
│   └── SecurityConfig.java          # Spring Security + JWT 配置(新增)
├── controller/
│   ├── ArticleController.java       # 文章接口
│   ├── AuthController.java          # 登录注册
│   ├── UserController.java          # 用户信息
│   ├── CommentController.java       # 评论
│   ├── UploadController.java        # 文件上传
│   └── admin/
│       ├── AdminArticleController   # 后台文章管理
│       ├── AdminCategoryController  # 后台分类管理
│       ├── AdminTagController       # 后台标签管理
│       ├── AdminCommentController   # 后台评论管理
│       ├── AdminFriendLinkController# 后台友链管理
│       └── AdminSiteController      # 后台站点配置
├── service/
│   ├── ArticleService.java
│   ├── UserService.java
│   ├── AuthService.java
│   ├── CommentService.java
│   ├── FileService.java
│   └── impl/
├── repository/
│   ├── ArticleMapper.java
│   ├── CategoryMapper.java
│   ├── TagMapper.java
│   ├── CommentMapper.java
│   ├── UserMapper.java
│   ├── FriendLinkMapper.java
│   └── SysConfigMapper.java
├── model/entity/
│   ├── Article.java
│   ├── Category.java
│   ├── Tag.java
│   ├── Comment.java
│   ├── User.java
│   ├── FriendLink.java
│   └── SysConfig.java
├── dto/
│   ├── LoginDTO.java                # 登录请求
│   ├── RegisterDTO.java             # 注册请求
│   ├── ArticleDTO.java              # 新增/修改文章
│   ├── ArticleQueryDTO.java         # 文章列表查询条件
│   ├── CommentDTO.java              # 评论请求
│   ├── PageDTO.java                 # 分页参数
│   └── ArticleVO.java               # 文章详情页（含作者、分类、标签、上一篇下一篇）
├── security/
│   ├── JwtTokenProvider.java        # JWT 生成和校验
│   ├── JwtAuthenticationFilter.java # JWT 过滤器
│   ├── UserDetailsImpl.java         # Spring Security 用户细节
│   └── SecurityConfig.java          # 安全配置（放行公开接口）
└── util/
    └── MarkdownUtil.java            # Markdown 转 HTML 工具
```

## 5. 关键实现细节

### 5.1 JWT 认证流程

1. 用户 POST `/api/auth/login` 提交 username + password
2. 服务端校验密码（BCrypt），生成 JWT token 返回
3. 前端将 token 存入 localStorage，后续请求放 `Authorization: Bearer <token>`
4. `JwtAuthenticationFilter` 拦截每个请求，解析 token，写入 SecurityContext

### 5.2 文章详情优化

- 浏览量：每次访问 `article/{id}` 时异步 +1（Redis INCR 定时回写 MySQL）
- 点赞：用 Redis Set 存储 `article:like:{articleId}` 的用户 ID，防重复点赞
- 上一篇/下一篇：同分类下按 create_time 排序查上下一条
- Markdown 转 HTML：存库时转换，还是查询时转换？→ 存 Markdown，读时转换，保留原始内容

### 5.3 评论

- 支持登录用户和游客评论（游客填昵称）
- 支持二级回复（parent_id 关联）
- 无登录时走简单验证码，防垃圾评论

### 5.4 文件上传

- 接口：`POST /api/upload`，接收 `multipart/form-data`
- 存储：本地磁盘 `uploads/` 目录，生产可改 OSS
- 返回：文件访问 URL
- 限：图片（jpg/png/gif/webp），最大 5MB

### 5.5 接口限流

- 登录接口：同一 IP 每分钟最多 10 次
- 评论接口：同一 IP 每分钟最多 3 次
- 用 Redis + AOP 注解实现

## 6. 实施步骤

| 步骤 | 内容 | 文件数 |
|------|------|--------|
| 1 | 新建 Entity + Mapper | 8 个实体 + 8 个 Mapper |
| 2 | Security 模块（JWT + 过滤器） | 4 个类 |
| 3 | Auth 模块（登录/注册） | 3 个类 |
| 4 | 文章模块（CRUD + 列表/详情/归档） | 4 个类 |
| 5 | 分类/标签模块 | 3 个类 |
| 6 | 评论模块 | 2 个类 |
| 7 | 文件上传模块 | 1 个类 |
| 8 | 后台管理模块 | 6 个 Controller |
| 9 | 友链 / 站点配置 | 3 个类 |
