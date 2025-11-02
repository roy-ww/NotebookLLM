# 微信登录配置说明

## 重要提示

微信扫码登录需要使用**微信开放平台的网站应用**，不能使用公众号AppID。

## 配置步骤

### 1. 在微信开放平台创建网站应用

1. 访问 [微信开放平台](https://open.weixin.qq.com/)
2. 登录并进入"管理中心"
3. 点击"网站应用" > "创建网站应用"
4. 填写应用信息：
   - 应用名称
   - 应用简介
   - 应用官网
   - 应用图标
5. 提交审核（通常需要1-3个工作日）

### 2. 配置授权回调域名

审核通过后：

1. 进入网站应用 > "开发信息"
2. 找到"授权回调域名"
3. 点击"设置"
4. 填写域名（只填域名，不要包含协议和端口）：
   - 本地开发：`localhost`
   - 生产环境：`yourdomain.com`
5. 下载验证文件并上传到服务器根目录

**重要：**
- 只填写域名部分，如：`localhost` 或 `example.com`
- 不要填写 `http://` 或 `https://`
- 不要填写端口号（如 `:3000`）
- 生产环境不能使用IP地址

### 3. 更新配置文件

在 `application.yml` 中配置网站应用的 AppID 和 AppSecret：

```yaml
wechat:
  app-id: 你的网站应用AppID
  app-secret: 你的网站应用AppSecret
  redirect-url: http://localhost:3000/  # 本地测试时使用
```

### 4. redirect_uri 配置说明

在代码中，`redirect_uri` 需要与授权回调域名匹配：

- 如果授权回调域名是 `localhost`，redirect_uri 可以是：
  - `http://localhost:3000/`
  - `http://localhost:3000/callback`
  
- 如果授权回调域名是 `example.com`，redirect_uri 可以是：
  - `https://example.com/`
  - `https://example.com/callback`
  - 但不能使用 `http://`（如果域名配置了HTTPS）

## 常见问题

### Q1: 提示"scope参数错误或没有scope权限"

**可能原因：**
1. AppID不是网站应用类型（可能是公众号）
2. 网站应用未审核通过
3. 授权回调域名未正确配置

**解决方法：**
1. 确认使用的是网站应用的AppID（不是公众号AppID）
2. 确认网站应用状态为"已通过"
3. 确认授权回调域名已正确配置

### Q2: 扫码后显示"redirect_uri参数错误"

**可能原因：**
1. redirect_uri 与配置的授权回调域名不匹配
2. redirect_uri 格式不正确
3. redirect_uri 未进行URL编码

**解决方法：**
1. 确保 redirect_uri 使用的域名与授权回调域名一致
2. 确保 redirect_uri 包含协议（http:// 或 https://）
3. 代码已自动进行URL编码，无需手动处理

### Q3: 如何判断AppID类型？

**网站应用AppID特点：**
- 在微信开放平台创建
- 应用类型显示为"网站应用"
- 支持 `snsapi_login` scope

**公众号AppID特点：**
- 在微信公众平台创建
- 应用类型显示为"服务号"或"订阅号"
- 不支持 `snsapi_login`，只能使用 `snsapi_base` 或 `snsapi_userinfo`

## 测试步骤

1. 确认网站应用已创建并审核通过
2. 确认授权回调域名已配置（本地测试填写 `localhost`）
3. 更新 `application.yml` 中的 AppID 和 AppSecret
4. 启动后端应用
5. 访问前端页面，测试扫码登录

## 注意事项

- 本地开发时，授权回调域名填写 `localhost` 即可
- 生产环境需要填写实际域名
- 确保 redirect_uri 使用的协议与域名配置一致（如果域名配置了HTTPS，redirect_uri也要用HTTPS）
- 授权回调域名一旦配置，修改需要审核，请谨慎填写

