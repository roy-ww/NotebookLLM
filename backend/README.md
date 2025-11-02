# Notebook LLM - 智能学习笔记系统

## 项目简介

Notebook LLM 是一个基于Spring Boot和PostgreSQL的智能学习笔记系统，支持微信登录注册等功能。

## 技术栈

- Java 17
- Spring Boot 3.2.0
- PostgreSQL 14+
- Spring Data JPA
- JWT认证
- 微信OAuth2.0

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.8+
- PostgreSQL 14+
- 微信开发者账号（用于微信登录功能）

### 2. 数据库初始化

#### 方法一：使用初始化脚本（推荐）

```bash
cd backend/src/main/resources/db
./init_database.sh
```

或者手动指定数据库连接信息：

```bash
DB_HOST=localhost DB_PORT=5432 DB_USER=postgres DB_PASSWORD=your_password ./init_database.sh
```

#### 方法二：手动创建数据库

1. 连接到PostgreSQL：
```bash
psql -h localhost -U postgres -d postgres
```

2. 创建数据库：
```sql
CREATE DATABASE notebook_llm;
```

3. 连接到新数据库：
```bash
\c notebook_llm
```

4. 执行初始化SQL：
```bash
psql -h localhost -U postgres -d notebook_llm -f src/main/resources/db/init.sql
```

### 3. 配置应用

编辑 `src/main/resources/application.yml`，配置以下信息：

- 数据库连接信息
- 微信AppID和AppSecret
- JWT密钥（生产环境必须修改）

### 4. 运行应用

```bash
cd backend
mvn spring-boot:run
```

或者编译后运行：

```bash
mvn clean package
java -jar target/notebook-llm-1.0.0.jar
```

## API接口

### 微信登录相关

- `GET /api/auth/wechat/url` - 获取微信授权URL
- `POST /api/auth/wechat/login` - 微信登录
- `GET /api/auth/wechat/callback` - 微信授权回调

### 用户信息相关

- `GET /api/user/me` - 获取当前用户信息（需要JWT Token）
- `GET /api/user/{userId}` - 根据ID获取用户信息
- `PUT /api/user/me` - 更新当前用户信息（需要JWT Token）

## 开发说明

### 数据库配置

默认数据库配置：
- 数据库名：`notebook_llm`
- 用户名：`postgres`
- 密码：`password`
- 主机：`localhost`
- 端口：`5432`

可通过环境变量或`application.yml`配置文件修改。

### 微信配置

在`application.yml`中配置微信AppID和AppSecret：

```yaml
wechat:
  app-id: your_app_id
  app-secret: your_app_secret
  redirect-url: http://localhost:8080/api/auth/wechat/callback
```

## 常见问题

### 数据库连接失败

1. 确保PostgreSQL服务已启动
2. 检查数据库连接信息是否正确
3. 确保数据库`notebook_llm`已创建
4. 检查数据库用户权限

### 编译错误

1. 确保JDK版本为17+
2. 运行 `mvn clean compile` 清理并重新编译
3. 检查Maven依赖是否正确下载

