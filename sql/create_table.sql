# 数据库初始化

-- 创建库
create database if not exists xiaoxinshu;

-- 切换库
use xiaoxinshu_dev;

-- 用户表
create table if not exists sys_user
(
    id            bigint auto_increment comment 'id' primary key,
    user_account  varchar(256)                       not null comment '账号',
    user_password varchar(512)                       not null comment '密码',
    user_name     varchar(256)                       null comment '用户昵称',
    user_avatar   varchar(512)                       null comment '用户头像',
    user_profile  varchar(512)                       null comment '用户简介',
    user_phone    varchar(11)                        null comment '用户手机号',
    user_email    varchar(256)                       null comment '用户邮箱',
    user_gender   tinyint  default 0                 not null comment '用户性别(0未知 1男 2女)',
    user_birthday date                               null comment '用户生日',
    user_role     tinyint  default 0                 not null comment '用户角色(0用户 1管理员)',
    remark        varchar(512)                       null comment '备注',
    edit_time     datetime default CURRENT_TIMESTAMP not null comment '编辑时间',
    create_time   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete     tinyint  default 0                 not null comment '是否删除',
    unique key uk_user_account (user_account),
    unique key uk_user_phone (user_phone),
    index idx_user_name (user_name)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 文章分类表
create table if not exists art_article_category
(
    `id`          bigint auto_increment comment 'id' primary key,
    `name`        varchar(60)                        not null comment '分类名称',
    `sort`        int      default 0                 not null comment '排序',
    `create_time` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    UNIQUE KEY `uk_name` (`name`) USING BTREE,
    index `idx_create_time` (`create_time`) USING BTREE
) comment '文章分类' collate = utf8mb4_unicode_ci;

