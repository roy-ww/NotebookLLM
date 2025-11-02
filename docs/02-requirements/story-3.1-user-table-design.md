# 用户故事 3.1: 用户信息表设计与创建

## 用户故事
**作为** 一个开发人员，  
**我需要** 创建用户信息表，  
**以便** 存储用户数据。

## 详细需求描述
设计并创建PostgreSQL数据库中的用户信息表，包含用户名、密码、昵称等最少必要字段，并考虑后续扩展性。

## 功能规格

### 前置条件
- PostgreSQL数据库已安装并运行
- 数据库访问权限已配置
- 数据库连接参数已确定

### 业务流程
1. 分析用户数据需求
2. 设计用户信息表结构
3. 确定字段类型和约束
4. 创建数据库表
5. 验证表结构正确性

### 输入
- 用户数据需求（用户名、密码、昵称等）
- 数据库规范要求
- 性能和安全要求

### 输出
- 用户信息表（PostgreSQL）
- 数据库初始化脚本
- 表结构文档

## 验收标准
- [ ] 表包含用户名、密码、昵称字段
- [ ] 表结构符合PostgreSQL规范
- [ ] 包含必要的约束（如唯一性约束）
- [ ] 包含必要的索引以提升查询性能
- [ ] 密码字段具备加密存储能力
- [ ] 表设计支持后续扩展

## 数据库表设计

### 表名: users
| 字段名 | 数据类型 | 约束 | 说明 |
|--------|----------|------|------|
| id | BIGSERIAL | PRIMARY KEY, NOT NULL | 主键，自增ID |
| username | VARCHAR(50) | UNIQUE, NOT NULL | 用户名，唯一 |
| password | VARCHAR(255) | NOT NULL | 密码，加密存储 |
| nickname | VARCHAR(100) | NOT NULL | 昵称 |
| wechat_openid | VARCHAR(50) | UNIQUE | 微信OpenId，唯一 |
| wechat_unionid | VARCHAR(50) | UNIQUE | 微信UnionId，唯一（可选） |
| avatar_url | VARCHAR(500) | | 头像URL（可选） |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | 更新时间 |

### 索引设计
- 主键索引: id (自动创建)
- 唯一索引: idx_users_username (username)
- 唯一索引: idx_users_wechat_openid (wechat_openid)
- 普通索引: idx_users_created_at (created_at)

### 约束设计
- 主键约束: id
- 唯一约束: username, wechat_openid
- 非空约束: id, username, password, nickname, created_at, updated_at

## 非功能性需求
- 性能：查询操作应在毫秒级完成
- 安全：密码字段支持加密存储
- 可扩展：表结构应支持后续字段扩展
- 可靠性：数据完整性得到保证

## 技术实现要点
- 使用PostgreSQL作为数据库
- 合理设计字段长度
- 实现数据加密机制（特别是密码）
- 创建适当的索引以优化查询
- 处理数据迁移和版本管理

## 创建SQL脚本示例
```sql
CREATE TABLE users (
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

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_wechat_openid ON users(wechat_openid);
CREATE INDEX idx_users_created_at ON users(created_at);

-- 更新时间触发器（可选）
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_users_updated_at 
    BEFORE UPDATE ON users 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();
```

## 异常情况
- 数据库连接失败
- 表已存在
- 字段约束冲突
- 权限不足