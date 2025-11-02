-- 创建数据库（如果不存在）
-- 注意：在PostgreSQL中，不能在事务中执行CREATE DATABASE
-- 这个脚本需要单独执行，或者使用psql命令行工具

-- 如果使用psql，先连接到postgres数据库，然后执行：
-- CREATE DATABASE notebook_llm;

-- 连接到notebook_llm数据库后，执行以下SQL创建表结构

-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(100) NOT NULL,
    wechat_openid VARCHAR(50) UNIQUE,
    wechat_unionid VARCHAR(50) UNIQUE,
    avatar_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_wechat_openid ON users(wechat_openid);
CREATE INDEX IF NOT EXISTS idx_users_created_at ON users(created_at);

-- 添加注释
COMMENT ON TABLE users IS '用户表';
COMMENT ON COLUMN users.id IS '用户ID';
COMMENT ON COLUMN users.username IS '用户名';
COMMENT ON COLUMN users.password IS '密码（加密后）';
COMMENT ON COLUMN users.nickname IS '昵称';
COMMENT ON COLUMN users.wechat_openid IS '微信OpenID';
COMMENT ON COLUMN users.wechat_unionid IS '微信UnionID';
COMMENT ON COLUMN users.avatar_url IS '头像URL';
COMMENT ON COLUMN users.created_at IS '创建时间';
COMMENT ON COLUMN users.updated_at IS '更新时间';

