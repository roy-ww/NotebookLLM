# 微信登录注册系统 - 后端技术方案

## 1. 项目概述

### 1.1 项目目标
开发一个支持微信登录和注册的后端系统，使用Java编写，数据存储在PostgreSQL数据库中。

### 1.2 项目范围
- 后端服务开发（Java + Spring Boot）
- PostgreSQL数据库设计与实现
- 微信登录注册功能
- 用户信息管理API
- 安全认证机制

## 2. 技术架构

### 2.1 技术栈选型
- **编程语言**: Java 17
- **Web框架**: Spring Boot 3.2+
- **Web服务器**: Embedded Tomcat
- **数据持久层**: Spring Data JPA + Hibernate
- **数据库**: PostgreSQL 14+
- **连接池**: HikariCP
- **认证授权**: Spring Security (后续扩展考虑)
- **微信SDK**: weixin-java-mp (WxJava)
- **JSON处理**: Jackson
- **构建工具**: Maven 3.8+
- **API文档**: SpringDoc (OpenAPI 3)
- **密码加密**: BCrypt

### 2.2 开发环境要求
- Java 17+ (推荐 OpenJDK)
- PostgreSQL 14+ (本地或Docker)
- Maven 3.8+
- IDE: IntelliJ IDEA 或 Eclipse

## 3. 系统架构设计

### 3.1 应用分层架构
```
Controller Layer      →  处理HTTP请求/响应
     ↓
Service Layer         →  业务逻辑处理
     ↓
Repository Layer      →  数据访问操作
     ↓
Entity Layer          →  数据实体模型
```

### 3.2 包结构设计
```
src/main/java/com/example/wechatlogin/
├── WeChatLoginApplication.java           # Spring Boot启动类
├── config/                              # 配置类
│   ├── SecurityConfig.java              # 安全配置
│   ├── DatabaseConfig.java              # 数据库配置
│   └── WeChatConfig.java                # 微信配置
├── controller/                          # 控制器层
│   ├── AuthController.java              # 认证控制器
│   ├── UserController.java              # 用户控制器
│   └── ApiExceptionHandler.java         # 全局异常处理
├── service/                             # 服务层
│   ├── impl/                           # 服务实现
│   │   ├── AuthServiceImpl.java        # 认证服务实现
│   │   ├── UserServiceImpl.java        # 用户服务实现
│   │   └── WeChatServiceImpl.java      # 微信服务实现
│   ├── AuthService.java                 # 认证服务接口
│   ├── UserService.java                 # 用户服务接口
│   └── WeChatService.java               # 微信服务接口
├── repository/                          # 数据访问层
│   └── UserRepository.java              # 用户数据访问接口
├── entity/                              # 实体类
│   └── User.java                        # 用户实体
├── dto/                                 # 数据传输对象
│   ├── request/                        # 请求对象
│   │   ├── WeChatAuthRequest.java      # 微信认证请求
│   │   └── UpdateUserInfoRequest.java  # 更新用户信息请求
│   └── response/                       # 响应对象
│       ├── ApiResponse.java            # 通用API响应
│       ├── UserInfoResponse.java       # 用户信息响应
│       └── AuthResponse.java           # 认证响应
├── exception/                           # 自定义异常
│   ├── BusinessException.java           # 业务异常
│   └── GlobalExceptionHandler.java     # 全局异常处理器
└── util/                                # 工具类
    ├── PasswordUtil.java               # 密码工具
    └── JwtUtil.java                    # JWT工具(可选)
```

## 4. API接口设计

### 4.1 微信授权相关API
```
POST /api/auth/wechat/authorize
→ 请求微信授权URL
Request: { "redirectUrl": "前端回调地址" }
Response: { "authUrl": "微信授权URL" }

GET /api/auth/wechat/callback
→ 微信授权回调接口
Request: { "code": "微信授权码", "state": "状态参数" }
Response: { "token": "认证令牌", "userInfo": {...} }

POST /api/auth/wechat/login
→ 微信登录接口
Request: { "code": "微信授权码" }
Response: { "token": "认证令牌", "userInfo": {...} }
```

### 4.2 用户管理API
```
GET /api/user/profile
→ 获取用户信息
Response: { "id": "...", "username": "...", "nickname": "...", ... }

PUT /api/user/profile
→ 更新用户信息
Request: { "nickname": "新昵称" }
Response: { "success": true, "message": "..." }
```

### 4.3 HTTP状态码规范
- 200: 请求成功
- 201: 创建成功
- 400: 请求参数错误
- 401: 未认证
- 403: 无权限
- 404: 资源不存在
- 500: 服务器内部错误

## 5. 数据库设计

### 5.1 数据库表结构

#### users表
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

-- 更新时间触发器
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

### 5.2 字段说明
- **id**: 主键，自增ID
- **username**: 用户名，唯一约束
- **password**: 加密后的密码（使用BCrypt）
- **nickname**: 用户昵称
- **wechat_openid**: 微信OpenId，唯一约束
- **wechat_unionid**: 微信UnionId，唯一约束（可选）
- **avatar_url**: 用户头像URL
- **created_at**: 创建时间
- **updated_at**: 更新时间（自动更新）

### 5.3 约束和索引
- 主键约束: id
- 唯一约束: username, wechat_openid
- 非空约束: id, username, password, nickname, created_at, updated_at
- 索引优化: username, wechat_openid, created_at

## 6. 服务架构设计

### 6.1 认证服务 (AuthService)
- 负责用户认证流程
- 处理微信授权码验证
- 生成和管理认证令牌
- 用户登录/注册逻辑

### 6.2 用户服务 (UserService)
- 管理用户基本信息
- 处理用户信息查询和更新
- 密码修改（如需要）
- 用户状态管理

### 6.3 微信服务 (WeChatService)
- 处理微信API调用
- 管理微信AppID和AppSecret
- 获取用户微信信息
- 验证授权码有效性

## 7. 数据流设计

### 7.1 微信登录数据流
```
1. 客户端 → 服务器: 发起微信登录请求
2. 服务器 → 微信API: 获取授权URL
3. 服务器 → 客户端: 返回授权URL
4. 客户端 → 微信页面: 跳转到微信授权页面
5. 用户 → 微信: 授权同意
6. 微信 → 服务器: 发送授权码到回调接口
7. 服务器 → 微信API: 用授权码换取access_token和用户信息
8. 服务器 → 数据库: 检查/创建用户记录
9. 服务器 → 客户端: 返回认证令牌和用户信息
```

### 7.2 用户信息更新数据流
```
1. 客户端 → 服务器: 发送更新用户信息请求（含认证令牌）
2. 服务器: 验证认证令牌
3. 服务器 → 数据库: 更新用户信息
4. 服务器 → 客户端: 返回更新结果
```

## 8. 业务逻辑设计

### 8.1 微信登录业务逻辑
1. 验证微信授权码的合法性
2. 向微信API请求获取用户信息
3. 根据微信OpenId查找本地用户
   - 如果找到用户：直接登录
   - 如果未找到：创建新用户并登录
4. 生成认证令牌返回给客户端

### 8.2 用户信息管理逻辑
1. 验证用户身份
2. 验证输入数据（如昵称长度等）
3. 更新数据库中的用户信息
4. 返回更新结果

### 8.3 安全验证逻辑
1. 验证微信授权码的有效性
2. 防止重放攻击
3. 用户身份验证
4. 数据输入验证

## 9. 安全方案

### 9.1 认证授权
- JWT Token认证（可选）
- 微信OAuth2.0流程验证
- 接口访问权限控制

### 9.2 数据安全
- 密码使用BCrypt加密存储
- 敏感信息脱敏处理
- HTTPS强制使用

### 9.3 API安全
- 接口访问频率限制
- 参数验证和清理
- SQL注入防护

### 9.4 微信API安全
- AppSecret安全存储
- 授权码验证
- 回调URL验证

## 10. 部署方案

### 10.1 容器化配置 (Docker)
```Dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/wechat-login-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### 10.2 环境变量配置
- DB_HOST: 数据库主机
- DB_PORT: 数据库端口
- DB_NAME: 数据库名
- DB_USERNAME: 数据库用户名
- DB_PASSWORD: 数据库密码
- WECHAT_APP_ID: 微信应用ID
- WECHAT_APP_SECRET: 微信应用密钥
- SERVER_PORT: 服务端口

### 10.3 配置文件 (application.yml)
```yaml
spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:wechat_login}
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:password}
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true

wechat:
  app-id: ${WECHAT_APP_ID:your_app_id}
  app-secret: ${WECHAT_APP_SECRET:your_app_secret}
  redirect-url: ${WECHAT_REDIRECT_URL:http://localhost:8080/api/auth/wechat/callback}

server:
  port: ${SERVER_PORT:8080}

logging:
  level:
    com.example.wechatlogin: DEBUG
    org.springframework.security: DEBUG
```

## 11. 监控日志

### 11.1 日志格式
- 使用SLF4J + Logback
- 结构化日志输出
- 包含请求ID追踪

### 11.2 监控指标
- API响应时间
- 错误率统计
- 用户活跃度
- 微信API调用统计

### 11.3 告警规则
- API错误率超过阈值
- 响应时间超过阈值
- 数据库连接异常

## 12. 开发规范

### 12.1 代码结构
- 遵循RESTful API设计原则
- 使用统一的错误处理机制
- 实现统一的响应格式

### 12.2 命名规范
- 类名: 驼峰命名，首字母大写 (UserService)
- 方法名: 驼峰命名，首字母小写 (getUserInfo)
- 常量: 全大写，下划线分隔 (DEFAULT_PAGE_SIZE)

### 12.3 注释要求
- 类和公共方法必须有JavaDoc注释
- 复杂逻辑需要行内注释
- API接口需要Swagger注释

### 12.4 测试规范
- 单元测试覆盖率不低于80%
- 集成测试覆盖主要业务流程
- 使用JUnit 5进行测试

## 13. 部署和运维

### 13.1 数据库迁移
- 使用Flyway进行数据库版本管理
- SQL脚本存储在resources/db/migration

### 13.2 健康检查
- 实现健康检查端点
- 数据库连接检查
- 外部服务依赖检查

### 13.3 性能优化
- 数据库查询优化
- 连接池配置优化
- 缓存机制(如需要)

这个技术方案详细描述了微信登录注册系统的后端实现方案，涵盖了技术选型、架构设计、数据库设计、API接口、安全方案等方面，为后续开发提供了完整指导。