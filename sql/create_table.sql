create database xiaoxinshu_dev;

comment on database xiaoxinshu_dev is '小新书本地';

\c xiaoxinshu_dev;

-- 用户表
create table if not exists sys_user
(
    id            bigserial primary key,
    user_account  varchar(256)                        not null,
    user_password varchar(512)                        not null,
    user_name     varchar(256),
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

create index idx_user_name on sys_user (user_name);

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

create index idx_create_time_category on art_article_category (create_time);

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

create index idx_create_time_tag on art_article_tag (create_time);

-- 注释
comment on table art_article_tag is '文章标签';
comment on column art_article_tag.id is 'id';
comment on column art_article_tag.name is '标签名称';
comment on column art_article_tag.create_time is '创建时间';
comment on column art_article_tag.update_time is '更新时间';