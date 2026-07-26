-- 分类表
CREATE TABLE IF NOT EXISTS `category`
(
    `id`          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`        VARCHAR(50) NOT NULL COMMENT '分类名称',
    `sort`        INT         DEFAULT 0 COMMENT '排序，越小越靠前',
    `create_time` DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='分类表';

-- 标签表
CREATE TABLE IF NOT EXISTS `tag`
(
    `id`          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`        VARCHAR(50) NOT NULL COMMENT '标签名称',
    `create_time` DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='标签表';

-- 文章表
CREATE TABLE IF NOT EXISTS `article`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `title`         VARCHAR(100) NOT NULL COMMENT '标题',
    `summary`       VARCHAR(500) DEFAULT NULL COMMENT '摘要',
    `content`       LONGTEXT     DEFAULT NULL COMMENT '正文 (Markdown)',
    `cover`         VARCHAR(255) DEFAULT NULL COMMENT '封面图 URL',
    `category_id`   BIGINT       DEFAULT NULL COMMENT '分类 ID',
    `user_id`       BIGINT       NOT NULL COMMENT '作者 ID',
    `view_count`    INT          DEFAULT 0 COMMENT '浏览量',
    `like_count`    INT          DEFAULT 0 COMMENT '点赞数',
    `comment_count` INT          DEFAULT 0 COMMENT '评论数',
    `status`        TINYINT      DEFAULT 0 COMMENT '0草稿 1发布 2私密',
    `is_top`        TINYINT      DEFAULT 0 COMMENT '是否置顶',
    `create_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`       TINYINT      DEFAULT 0 COMMENT '逻辑删除 0正常 1删除',
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='文章表';

-- 文章-标签关联表
CREATE TABLE IF NOT EXISTS `article_tag`
(
    `article_id` BIGINT NOT NULL COMMENT '文章 ID',
    `tag_id`     BIGINT NOT NULL COMMENT '标签 ID',
    PRIMARY KEY (`article_id`, `tag_id`),
    KEY `idx_tag_id` (`tag_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='文章-标签关联表';

-- 评论表
CREATE TABLE IF NOT EXISTS `comment`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `article_id`   BIGINT       NOT NULL COMMENT '文章 ID',
    `user_id`      BIGINT       DEFAULT NULL COMMENT '评论人 ID (登录用户)',
    `parent_id`    BIGINT       DEFAULT NULL COMMENT '父评论 ID，为空则为一级评论',
    `visitor_name` VARCHAR(50)  DEFAULT NULL COMMENT '游客昵称 (未登录)',
    `content`      TEXT         NOT NULL COMMENT '评论内容',
    `status`       TINYINT      DEFAULT 0 COMMENT '0待审 1通过 2拒绝',
    `create_time`  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_article_id` (`article_id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='评论表';

-- 友链表
CREATE TABLE IF NOT EXISTS `friend_link`
(
    `id`   BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` VARCHAR(50)  NOT NULL COMMENT '网站名称',
    `url`  VARCHAR(255) NOT NULL COMMENT '网站地址',
    `logo` VARCHAR(255) DEFAULT NULL COMMENT 'Logo URL',
    `sort` INT          DEFAULT 0 COMMENT '排序',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='友链表';

-- 系统配置表
CREATE TABLE IF NOT EXISTS `sys_config`
(
    `id`           BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
    `config_key`   VARCHAR(50) NOT NULL COMMENT '配置键',
    `config_value` TEXT        DEFAULT NULL COMMENT '配置值',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='系统配置表';
