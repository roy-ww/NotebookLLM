package com.notebook.service;

import com.notebook.dto.request.WeChatAuthRequest;
import com.notebook.dto.response.AuthResponse;

/**
 * 认证服务接口
 * 处理微信登录注册相关的业务逻辑
 */
public interface AuthService {

    /**
     * 处理微信登录
     * @param request 微信认证请求
     * @return 认证响应
     */
    AuthResponse processWeChatLogin(WeChatAuthRequest request);

    /**
     * 获取微信授权URL
     * @param redirectUri 重定向地址
     * @return 授权URL
     */
    String getWeChatAuthUrl(String redirectUri);

    /**
     * 处理微信授权回调
     * @param code 授权码
     * @param state 状态参数
     * @return 认证响应
     */
    AuthResponse handleWeChatCallback(String code, String state);

    /**
     * 获取微信扫码登录二维码图片URL
     * @param redirectUri 重定向地址
     * @param state 状态参数
     * @return 二维码图片URL
     */
    String getWeChatQrCodeImageUrl(String redirectUri, String state);
}