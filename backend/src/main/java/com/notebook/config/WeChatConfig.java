package com.notebook.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 微信配置类
 * 从配置文件中读取微信相关配置
 */
@Configuration
@ConfigurationProperties(prefix = "wechat")
public class WeChatConfig {

    /**
     * 微信AppID
     */
    private String appId;

    /**
     * 微信AppSecret
     */
    private String appSecret;

    /**
     * 微信授权重定向URL
     */
    private String redirectUrl;

    /**
     * 微信OAuth2.0授权URL
     */
    private static final String WECHAT_AUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize";

    /**
     * 微信AccessToken获取URL
     */
    private static final String WECHAT_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";

    /**
     * 微信用户信息获取URL
     */
    private static final String WECHAT_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo";

    /**
     * 获取微信授权URL
     */
    public String getAuthUrl(String redirectUri) {
        return String.format("%s?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect",
                WECHAT_AUTH_URL, appId, redirectUri);
    }

    /**
     * 获取AccessToken URL
     */
    public String getAccessTokenUrl(String code) {
        return String.format("%s?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                WECHAT_ACCESS_TOKEN_URL, appId, appSecret, code);
    }

    /**
     * 获取用户信息URL
     */
    public String getUserInfoUrl(String accessToken, String openId) {
        return String.format("%s?access_token=%s&openid=%s",
                WECHAT_USER_INFO_URL, accessToken, openId);
    }

    // Getter and Setter methods
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}

