-- 创建数据库
create schema if not exists xiaoxinshu_interface collate utf8mb4_unicode_ci;

-- 使用数据库
use xiaoxinshu_interface;

-- 图片表
create table if not exists image
(
    `id`          bigint auto_increment comment 'id' primary key,
    `url`         varchar(256)                       not null comment '图片访问 URL',
    `type`        varchar(50)                        not null comment '图片类型',
    `create_time` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    index idx_type (type)
) comment '图片' collate = utf8mb4_unicode_ci;
