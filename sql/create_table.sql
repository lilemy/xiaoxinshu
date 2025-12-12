drop database if exists xiaoxinshu_dev;
create database xiaoxinshu_dev;

comment on database xiaoxinshu_dev is '小新书本地';

-- 用户表
create table if not exists sys_user
(
    id            bigserial primary key,
    user_account  varchar(256)                        not null,
    user_password varchar(512)                        not null,
    user_name     varchar(256)                        not null,
    user_avatar   varchar(512),
    user_profile  varchar(512),
    user_phone    varchar(11),
    user_email    varchar(256),
    user_gender   smallint  default 0                 not null,
    user_birthday date,
    user_role     smallint  default 0                 not null,
    remark        varchar(512),
    edit_time     timestamp default CURRENT_TIMESTAMP not null,
    create_time   timestamp default CURRENT_TIMESTAMP not null,
    update_time   timestamp default CURRENT_TIMESTAMP not null,
    is_delete     smallint  default 0                 not null,
    constraint uk_user_account unique (user_account),
    constraint uk_user_phone unique (user_phone)
);

create index idx_sys_user_user_account on sys_user (user_account);
create index idx_sys_user_user_name on sys_user (user_name);

-- 注释
comment on table sys_user is '用户';
comment on column sys_user.id is 'id';
comment on column sys_user.user_account is '账号';
comment on column sys_user.user_password is '密码';
comment on column sys_user.user_name is '用户昵称';
comment on column sys_user.user_avatar is '用户头像';
comment on column sys_user.user_profile is '用户简介';
comment on column sys_user.user_phone is '用户手机号';
comment on column sys_user.user_email is '用户邮箱';
comment on column sys_user.user_gender is '用户性别(0未知 1男 2女)';
comment on column sys_user.user_birthday is '用户生日';
comment on column sys_user.user_role is '用户角色(0用户 1管理员)';
comment on column sys_user.remark is '备注';
comment on column sys_user.edit_time is '编辑时间';
comment on column sys_user.create_time is '创建时间';
comment on column sys_user.update_time is '更新时间';
comment on column sys_user.is_delete is '是否删除';


-- 文章分类表
create table if not exists art_article_category
(
    id          bigserial primary key,
    name        varchar(60)                         not null,
    sort        int       default 0                 not null,
    create_time timestamp default CURRENT_TIMESTAMP not null,
    update_time timestamp default CURRENT_TIMESTAMP not null,
    constraint uk_name_category unique (name)
);

create index idx_art_article_category_create_time on art_article_category (create_time);

-- 注释
comment on table art_article_category is '文章分类';
comment on column art_article_category.id is 'id';
comment on column art_article_category.name is '分类名称';
comment on column art_article_category.sort is '排序';
comment on column art_article_category.create_time is '创建时间';
comment on column art_article_category.update_time is '更新时间';


-- 文章标签表
create table if not exists art_article_tag
(
    id          bigserial primary key,
    name        varchar(60)                         not null,
    create_time timestamp default CURRENT_TIMESTAMP not null,
    update_time timestamp default CURRENT_TIMESTAMP not null,
    constraint uk_name_tag unique (name)
);

create index idx_art_article_tag_create_time on art_article_tag (create_time);

-- 注释
comment on table art_article_tag is '文章标签';
comment on column art_article_tag.id is 'id';
comment on column art_article_tag.name is '标签名称';
comment on column art_article_tag.create_time is '创建时间';
comment on column art_article_tag.update_time is '更新时间';


-- 文章表
create table art_article
(
    id          bigserial primary key,
    title       varchar(120)                        not null,
    cover       varchar(120),
    summary     varchar(160),
    user_id     bigint                              not null,
    edit_time   timestamp default CURRENT_timestamp not null,
    create_time timestamp default CURRENT_timestamp not null,
    update_time timestamp default CURRENT_timestamp not null,
    is_delete   smallint  default 0                 not null,
    read_num    int       default 0                 not null
);

-- 索引
create index idx_art_article_create_time on art_article (create_time);
create index idx_art_article_user_id on art_article (user_id);

-- 表和字段注释
comment on table art_article is '文章';
comment on column art_article.id is '文章id';
comment on column art_article.title is '文章标题';
comment on column art_article.cover is '文章封面';
comment on column art_article.summary is '文章摘要';
comment on column art_article.user_id is '用户id';
comment on column art_article.edit_time is '编辑时间';
comment on column art_article.create_time is '创建时间';
comment on column art_article.update_time is '更新时间';
comment on column art_article.is_delete is '是否删除';
comment on column art_article.read_num is '阅读次数';


-- 文章内容表
create table art_article_content
(
    id         bigserial primary key,
    article_id bigint not null,
    content    text
);

-- 索引
create index idx_art_article_content_article_id on art_article_content (article_id);

-- 注释
comment on table art_article_content is '文章内容表';
comment on column art_article_content.id is '文章内容id';
comment on column art_article_content.article_id is '文章id';
comment on column art_article_content.content is '文章正文';


-- 文章所属分类关联表
create table art_article_category_rel
(
    id          bigserial primary key,
    article_id  bigint not null,
    category_id bigint not null
);

-- 索引
create index idx_art_article_category_rel_article_id on art_article_category_rel (article_id);
create index idx_art_article_category_rel_category_id on art_article_category_rel (category_id);

-- 注释
comment on table art_article_category_rel is '文章所属分类';
comment on column art_article_category_rel.id is 'id';
comment on column art_article_category_rel.article_id is '文章id';
comment on column art_article_category_rel.category_id is '分类id';


-- 文章标签关联表
create table art_article_tag_rel
(
    id         bigserial primary key,
    article_id bigint not null,
    tag_id     bigint not null
);

-- 索引
create index idx_art_article_tag_rel_article_id on art_article_tag_rel (article_id);
create index idx_art_article_tag_rel_tag_id on art_article_tag_rel (tag_id);

-- 注释
comment on table art_article_tag_rel is '文章对应标签';
comment on column art_article_tag_rel.id is 'id';
comment on column art_article_tag_rel.article_id is '文章id';
comment on column art_article_tag_rel.tag_id is '标签id';
