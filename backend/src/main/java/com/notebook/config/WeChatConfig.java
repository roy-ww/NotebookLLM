package com.notebook.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
     * 微信二维码创建URL（网页授权）
     */
    private static final String WECHAT_QRCODE_URL = "https://open.weixin.qq.com/connect/qrconnect";

    /**
     * 微信网页授权二维码URL（用于生成可直接访问的二维码图片）
     */
    public String getQrCodeUrl(String redirectUri, String state) {
        try {
            // 对redirect_uri和state进行URL编码
            String encodedRedirectUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8.name());
            String encodedState = URLEncoder.encode(state != null ? state : "STATE", StandardCharsets.UTF_8.name());
            
            // 使用微信网页授权二维码（qrconnect端点用于网站应用扫码登录）
            // scope=snsapi_login 必须使用网站应用的AppID
            return String.format("%s?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login&state=%s#wechat_redirect",
                    WECHAT_QRCODE_URL, appId, encodedRedirectUri, encodedState);
        } catch (Exception e) {
            // 如果编码失败，使用原始值（不应该发生）
            return String.format("%s?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login&state=%s#wechat_redirect",
                    WECHAT_QRCODE_URL, appId, redirectUri, state != null ? state : "STATE");
        }
    }

    /**
     * 生成二维码图片URL（用于显示二维码）
     */
    public String getQrCodeImageUrl(String qrCodeUrl) {
        // 使用第三方服务生成二维码图片
        // 这里我们使用在线二维码生成服务
        try {
            return "https://api.qrserver.com/v1/create-qr-code/?size=300x300&data=" + 
                   URLEncoder.encode(qrCodeUrl, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            // 如果编码失败，直接返回（可能包含特殊字符的问题）
            return "https://api.qrserver.com/v1/create-qr-code/?size=300x300&data=" + qrCodeUrl.replace(" ", "%20");
        }
    }

    /**
     * 获取微信授权URL（公众号网页授权，只能在微信内打开）
     */
    public String getAuthUrl(String redirectUri) {
        try {
            // 对redirect_uri进行URL编码
            String encodedRedirectUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8.name());
            // 公众号网页授权使用 snsapi_userinfo 或 snsapi_base
            return String.format("%s?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect",
                    WECHAT_AUTH_URL, appId, encodedRedirectUri);
        } catch (Exception e) {
            // 如果编码失败，使用原始值
            return String.format("%s?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect",
                    WECHAT_AUTH_URL, appId, redirectUri);
        }
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

