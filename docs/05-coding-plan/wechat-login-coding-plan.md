# 微信登录注册系统 - 编码计划

## 1. 代码生成范围概览

### 1.1 预计生成的代码文件统计
- **Java后端代码**: 20-25个文件
- **配置文件**: 3-5个文件
- **SQL脚本**: 2-3个文件
- **测试代码**: 10-15个文件
- **总文件数**: 约40-50个文件

### 1.2 代码模块分类
- **实体层 (Entity)**: 1个文件 (User.java)
- **数据访问层 (Repository)**: 1个文件 (UserRepository.java)  
- **服务层 (Service)**: 6个文件 (接口+实现)
- **控制器层 (Controller)**: 2个文件
- **数据传输对象 (DTO)**: 6个文件 (请求+响应)
- **配置类 (Config)**: 3个文件
- **工具类 (Util)**: 2个文件
- **异常处理 (Exception)**: 2个文件
- **测试代码 (Test)**: 10-15个文件
- **配置文件**: 3-5个文件
- **SQL脚本**: 2-3个文件

## 2. 生成进度规划

### 2.1 第一阶段：项目基础架构 (第1天)
- 项目初始化和依赖配置
- 基础包结构创建
- 主启动类创建
- 基本配置文件设置

### 2.2 第二阶段：数据模型层 (第1天)
- User实体类创建
- 数据库配置和连接
- Repository接口实现

### 2.3 第三阶段：服务层核心逻辑 (第2天)
- Service接口定义
- Service实现类开发
- 微信认证逻辑实现

### 2.4 第四阶段：控制器层API (第2天)
- Controller类创建
- API接口实现
- 请求响应对象定义

### 2.5 第五阶段：安全和配置 (第3天)
- 安全配置
- 微信相关配置
- 全局异常处理

### 2.6 第六阶段：测试和优化 (第3-4天)
- 单元测试编写
- 集成测试编写
- 代码优化和文档

## 3. 目录结构计划

### 3.1 后端项目目录结构
```
backend/
├── pom.xml                    # Maven依赖配置文件
├── Dockerfile                 # Docker容器化配置
├── docker-compose.yml         # Docker Compose配置
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── wechatlogin/
│   │   │               ├── WeChatLoginApplication.java
│   │   │               ├── config/
│   │   │               │   ├── SecurityConfig.java
│   │   │               │   ├── DatabaseConfig.java
│   │   │               │   └── WeChatConfig.java
│   │   │               ├── controller/
│   │   │               │   ├── AuthController.java
│   │   │               │   ├── UserController.java
│   │   │               │   └── ApiExceptionHandler.java
│   │   │               ├── service/
│   │   │               │   ├── AuthService.java
│   │   │               │   ├── UserService.java
│   │   │               │   ├── WeChatService.java
│   │   │               │   ├── impl/
│   │   │               │   │   ├── AuthServiceImpl.java
│   │   │               │   │   ├── UserServiceImpl.java
│   │   │               │   │   └── WeChatServiceImpl.java
│   │   │               ├── repository/
│   │   │               │   └── UserRepository.java
│   │   │               ├── entity/
│   │   │               │   └── User.java
│   │   │               ├── dto/
│   │   │               │   ├── request/
│   │   │               │   │   ├── WeChatAuthRequest.java
│   │   │               │   │   └── UpdateUserInfoRequest.java
│   │   │               │   └── response/
│   │   │               │       ├── ApiResponse.java
│   │   │               │       ├── UserInfoResponse.java
│   │   │               │       └── AuthResponse.java
│   │   │               ├── exception/
│   │   │               │   ├── BusinessException.java
│   │   │               │   └── GlobalExceptionHandler.java
│   │   │               └── util/
│   │   │                   ├── PasswordUtil.java
│   │   │                   └── JwtUtil.java
│   │   └── resources/
│   │       ├── application.yml              # 主配置文件
│   │       ├── application-dev.yml          # 开发环境配置
│   │       ├── application-prod.yml         # 生产环境配置
│   │       ├── db/
│   │       │   └── migration/
│   │       │       └── V1__create_users_table.sql
│   │       └── schema.sql                   # 数据库Schema
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           └── wechatlogin/
│       │               ├── service/
│       │               │   ├── UserServiceTest.java
│       │               │   ├── AuthServiceTest.java
│       │               │   └── WeChatServiceTest.java
│       │               ├── controller/
│       │               │   ├── AuthControllerTest.java
│       │               │   └── UserControllerTest.java
│       │               └── integration/
│       │                   └── WeChatLoginIntegrationTest.java
│       └── resources/
│           └── application-test.yml         # 测试环境配置
```

## 4. 代码文件规划

### 4.1 核心实体文件
- **路径**: `src/main/java/com/example/wechatlogin/entity/User.java`
- **职责**: 用户实体类，对应数据库users表
- **预估代码量**: 100-150行
- **包含**: JPA注解、字段定义、getter/setter、构造函数

### 4.2 数据访问层文件
- **路径**: `src/main/java/com/example/wechatlogin/repository/UserRepository.java`
- **职责**: 用户数据访问接口
- **预估代码量**: 30-50行
- **包含**: Spring Data JPA接口定义、自定义查询方法

### 4.3 服务层接口文件
- **路径**: `src/main/java/com/example/wechatlogin/service/AuthService.java`
- **职责**: 认证服务接口定义
- **预估代码量**: 50-80行
- **包含**: 微信登录、用户认证等方法定义

### 4.4 服务层实现文件
- **路径**: `src/main/java/com/example/wechatlogin/service/impl/AuthServiceImpl.java`
- **职责**: 认证服务接口实现
- **预估代码量**: 150-250行
- **包含**: 微信登录逻辑、用户认证逻辑、异常处理

### 4.5 控制器层文件
- **路径**: `src/main/java/com/example/wechatlogin/controller/AuthController.java`
- **职责**: 认证相关API接口
- **预估代码量**: 100-150行
- **包含**: 微信登录API、授权回调API

### 4.6 数据传输对象文件
- **路径**: `src/main/java/com/example/wechatlogin/dto/request/WeChatAuthRequest.java`
- **职责**: 微信认证请求数据对象
- **预估代码量**: 20-40行
- **包含**: 请求参数定义、验证注解

## 5. 类依赖关系图

### 5.1 服务层依赖关系
```
AuthController
    ↓ (依赖)
AuthService → WeChatService, UserService
    ↓ (实现)
AuthServiceImpl
```

### 5.2 数据访问依赖关系
```
UserService
    ↓ (依赖)
UserRepository
    ↓ (实现JPA)
JPA/Hibernate → PostgreSQL
```

### 5.3 配置依赖关系
```
WeChatConfig → 外部配置
    ↓ (提供)
WeChatService
```

## 6. 代码拆分策略

### 6.1 按功能拆分
- **认证模块**: AuthController, AuthService及其实现
- **用户模块**: UserController, UserService及其实现
- **微信模块**: WeChatService及实现
- **数据模块**: Entity, Repository层

### 6.2 按层次拆分
- **表现层**: Controller类 (处理HTTP请求)
- **业务层**: Service接口和实现类 (业务逻辑)
- **数据层**: Entity和Repository (数据访问)
- **传输层**: DTO类 (数据传输对象)

### 6.3 按职责拆分
- **配置类**: 所有Config类 (系统配置)
- **工具类**: 所有Util类 (通用工具方法)
- **异常类**: 所有Exception类 (异常处理)
- **安全类**: 安全相关配置和实现

## 7. 包结构设计

### 7.1 主包结构
```
com.example.wechatlogin
├── config          # 配置类
├── controller      # 控制器层
├── service         # 服务层
├── repository      # 数据访问层
├── entity          # 实体类
├── dto             # 数据传输对象
├── exception       # 异常处理
├── util            # 工具类
└── (主启动类)      # WeChatLoginApplication.java
```

### 7.2 DTO子包结构
```
dto/
├── request/        # 请求对象
└── response/       # 响应对象
```

### 7.3 Service子包结构
```
service/
├── impl/          # 服务实现类
└── (服务接口)     # 服务接口定义
```

## 8. 接口依赖分析

### 8.1 Controller依赖
- AuthController依赖AuthService
- UserController依赖UserService
- 所有Controller依赖DTO类

### 8.2 Service依赖
- AuthService依赖WeChatService和UserService
- UserService依赖UserRepository
- WeChatService可能依赖外部微信SDK

### 8.3 Repository依赖
- UserRepository依赖User实体类
- Repository层通过Spring Data JPA依赖数据库

## 9. 业务逻辑概要

### 9.1 微信登录业务逻辑
1. Controller接收微信授权码
2. AuthService调用WeChatService验证授权码
3. WeChatService与微信API交互获取用户信息
4. AuthService调用UserService处理用户创建/更新
5. UserService操作数据库存储/更新用户信息
6. AuthService生成认证响应返回给Controller

### 9.2 用户信息管理逻辑
1. Controller验证用户认证状态
2. UserService根据用户ID查询/更新用户信息
3. Repository层执行数据库操作
4. 返回结果给Controller

## 10. 配置和资源

### 10.1 配置文件
- `application.yml`: 主配置文件，包含数据库、微信、服务器配置
- `application-dev.yml`: 开发环境特定配置
- `application-prod.yml`: 生产环境特定配置
- `application-test.yml`: 测试环境配置

### 10.2 数据库脚本
- `V1__create_users_table.sql`: 创建用户表
- `schema.sql`: 数据库Schema定义
- 可能的后续版本SQL脚本

## 11. 生成顺序建议

### 11.1 优先级顺序
1. **最高优先级**: Entity实体类 (User.java)
2. **高优先级**: Repository接口 (UserRepository.java)
3. **高优先级**: Service接口定义
4. **高优先级**: DTO类定义
5. **中优先级**: Service实现类
6. **中优先级**: Controller类
7. **中优先级**: 配置类
8. **低优先级**: 工具类和异常类
9. **最后**: 测试代码

### 11.2 依赖顺序
1. 首先生成无依赖的类 (Entity, DTO)
2. 然后生成依赖这些类的接口 (Repository, Service接口)
3. 最后生成实现类和Controller

## 12. 潜在问题预警

### 12.1 文件位置问题
- **问题**: Service实现类需要放在impl包中
- **解决方案**: 严格按照包结构规划存放文件

### 12.2 依赖关系问题
- **问题**: 循环依赖 (如AuthService和UserService相互依赖)
- **解决方案**: 重构业务逻辑，确保依赖关系单向

### 12.3 代码拆分问题
- **问题**: 单个类文件过大
- **解决方案**: 将复杂业务逻辑拆分为多个服务类

### 12.4 数据库操作问题
- **问题**: JPA映射与数据库表结构不匹配
- **解决方案**: 严格按数据库设计文档创建实体类

### 12.5 配置管理问题
- **问题**: 敏感信息硬编码
- **解决方案**: 使用环境变量或配置文件管理敏感信息

## 13. 编码规范要求

### 13.1 命名规范
- 类名: 驼峰命名，首字母大写 (UserService)
- 方法名: 驼峰命名，首字母小写 (getUserInfo)
- 常量: 全大写，下划线分隔 (DEFAULT_PAGE_SIZE)
- 包名: 全小写，点分隔 (com.example.wechatlogin.service)

### 13.2 代码结构规范
- 类中成员变量、方法按访问级别排序 (public, protected, private)
- 相似功能的方法分组放在一起
- 重要的业务方法需要JavaDoc注释

### 13.3 注释规范
- 类和公共方法必须有JavaDoc注释
- 复杂业务逻辑需要行内注释
- API接口需要Swagger注释

## 14. 项目依赖管理

### 14.1 Maven依赖项
- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- PostgreSQL Driver
- HikariCP (连接池)
- Spring Boot Starter Validation
- WeChat SDK (WxJava)
- JUnit 5 (测试)
- Spring Boot Starter Test
- BCrypt (密码加密)

### 14.2 版本控制
- 所有代码文件需要在Git版本控制下
- 使用Maven进行依赖版本管理
- 数据库脚本需要版本控制