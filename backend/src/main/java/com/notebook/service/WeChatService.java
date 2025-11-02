package com.notebook.service;

import com.notebook.dto.response.WeChatAccessTokenResponse;
import com.notebook.dto.response.WeChatUserInfoResponse;

/**
 * 微信服务接口
 * 处理与微信API交互的业务逻辑
 */
public interface WeChatService {

    /**
     * 获取微信授权URL
     * @param redirectUri 重定向地址
     * @return 授权URL
     */
    String getWeChatAuthUrl(String redirectUri);

    /**
     * 根据授权码获取AccessToken
     * @param code 授权码
     * @return AccessToken响应
     */
    WeChatAccessTokenResponse getAccessTokenByCode(String code);

    /**
     * 根据AccessToken和OpenId获取用户信息
     * @param accessToken 访问令牌
     * @param openId 用户OpenID
     * @return 用户信息响应
     */
    WeChatUserInfoResponse getUserInfo(String accessToken, String openId);
}