package com.notebook.controller;

import com.notebook.dto.request.WeChatAuthRequest;
import com.notebook.dto.response.ApiResponse;
import com.notebook.dto.response.AuthResponse;
import com.notebook.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 处理微信登录相关的HTTP请求
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    /**
     * 获取微信授权URL
     * GET /api/auth/wechat/url?redirectUri={redirectUri}
     */
    @GetMapping("/wechat/url")
    public ApiResponse<Map<String, String>> getWeChatAuthUrl(
            @RequestParam(value = "redirectUri", required = false) String redirectUri) {
        try {
            // 如果没有提供redirectUri，使用默认的
            if (redirectUri == null || redirectUri.trim().isEmpty()) {
                redirectUri = "http://localhost:8080/api/auth/wechat/callback";
            }

            String authUrl = authService.getWeChatAuthUrl(redirectUri);
            
            Map<String, String> result = new HashMap<>();
            result.put("authUrl", authUrl);
            result.put("redirectUri", redirectUri);

            logger.info("获取微信授权URL成功: redirectUri={}", redirectUri);
            return ApiResponse.success("获取授权URL成功", result);
        } catch (Exception e) {
            logger.error("获取微信授权URL失败", e);
            return ApiResponse.error("获取授权URL失败: " + e.getMessage());
        }
    }

    /**
     * 微信登录
     * POST /api/auth/wechat/login
     */
    @PostMapping("/wechat/login")
    public ApiResponse<AuthResponse> weChatLogin(@Valid @RequestBody WeChatAuthRequest request) {
        try {
            logger.info("收到微信登录请求: code={}", request.getCode());
            
            AuthResponse response = authService.processWeChatLogin(request);
            
            logger.info("微信登录成功: userId={}, username={}", 
                    response.getUserInfo().getId(), response.getUserInfo().getUsername());
            
            return ApiResponse.success("登录成功", response);
        } catch (Exception e) {
            logger.error("微信登录失败", e);
            return ApiResponse.error("登录失败: " + e.getMessage());
        }
    }

    /**
     * 微信授权回调
     * GET /api/auth/wechat/callback?code={code}&state={state}
     */
    @GetMapping("/wechat/callback")
    public ApiResponse<AuthResponse> weChatCallback(
            @RequestParam("code") String code,
            @RequestParam(value = "state", required = false) String state) {
        try {
            logger.info("收到微信授权回调: code={}, state={}", code, state);
            
            AuthResponse response = authService.handleWeChatCallback(code, state);
            
            logger.info("微信授权回调处理成功: userId={}, username={}", 
                    response.getUserInfo().getId(), response.getUserInfo().getUsername());
            
            // 这里可以重定向到前端页面，携带token
            // 或者返回一个包含token的响应页面
            return ApiResponse.success("授权成功", response);
        } catch (Exception e) {
            logger.error("微信授权回调处理失败", e);
            return ApiResponse.error("授权失败: " + e.getMessage());
        }
    }

    /**
     * 获取微信扫码登录二维码
     * GET /api/auth/wechat/qrcode?redirectUri={redirectUri}
     * 
     * 注意：此接口需要使用微信开放平台的"网站应用"AppID
     * 如果AppID是公众号类型，会出现"scope参数错误"的提示
     */
    @GetMapping("/wechat/qrcode")
    public ApiResponse<Map<String, String>> getWeChatQrCode(
            @RequestParam(value = "redirectUri", required = false) String redirectUri) {
        try {
            // 如果没有提供redirectUri，使用默认的
            if (redirectUri == null || redirectUri.trim().isEmpty()) {
                redirectUri = "http://localhost:3000/";
            }

            // 生成state参数（可以使用UUID确保唯一性）
            String state = java.util.UUID.randomUUID().toString();

            // 获取二维码图片URL
            String qrCodeImageUrl = authService.getWeChatQrCodeImageUrl(redirectUri, state);
            
            // 同时返回原始的授权URL（用于调试）
            String authUrl = authService.getWeChatAuthUrl(redirectUri);
            
            Map<String, String> result = new HashMap<>();
            result.put("qrcodeUrl", qrCodeImageUrl);
            result.put("redirectUri", redirectUri);
            result.put("state", state);
            result.put("authUrl", authUrl); // 用于调试

            logger.info("获取微信二维码成功: redirectUri={}, state={}", redirectUri, state);
            
            return ApiResponse.success("获取二维码成功", result);
        } catch (Exception e) {
            logger.error("获取微信二维码失败", e);
            String errorMsg = "获取二维码失败: " + e.getMessage();
            
            // 提供更详细的错误提示
            if (e.getMessage() != null && e.getMessage().contains("scope")) {
                errorMsg += "\n提示：请确认AppID是否为微信开放平台的网站应用类型，且已在开放平台配置授权回调域名。";
            }
            
            return ApiResponse.error(errorMsg);
        }
    }
}

