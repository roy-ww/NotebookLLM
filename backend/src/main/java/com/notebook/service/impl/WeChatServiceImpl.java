package com.notebook.service.impl;

import com.notebook.config.WeChatConfig;
import com.notebook.dto.response.WeChatAccessTokenResponse;
import com.notebook.dto.response.WeChatUserInfoResponse;
import com.notebook.exception.BusinessException;
import com.notebook.service.WeChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 微信服务实现类
 * 处理与微信API交互的业务逻辑
 */
@Service
public class WeChatServiceImpl implements WeChatService {

    private static final Logger logger = LoggerFactory.getLogger(WeChatServiceImpl.class);

    @Autowired
    private WeChatConfig weChatConfig;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 获取微信授权URL
     */
    @Override
    public String getWeChatAuthUrl(String redirectUri) {
        return weChatConfig.getAuthUrl(redirectUri);
    }

    /**
     * 根据授权码获取AccessToken
     */
    @Override
    public WeChatAccessTokenResponse getAccessTokenByCode(String code) {
        try {
            String url = weChatConfig.getAccessTokenUrl(code);
            logger.info("请求微信AccessToken: {}", url.replace(weChatConfig.getAppSecret(), "***"));

            String response = restTemplate.getForObject(url, String.class);
            logger.debug("微信AccessToken响应: {}", response);

            WeChatAccessTokenResponse tokenResponse = objectMapper.readValue(response, WeChatAccessTokenResponse.class);

            if (!tokenResponse.isSuccess()) {
                logger.error("获取微信AccessToken失败: errcode={}, errmsg={}", 
                        tokenResponse.getErrcode(), tokenResponse.getErrmsg());
                throw new BusinessException("获取微信AccessToken失败: " + tokenResponse.getErrmsg());
            }

            return tokenResponse;
        } catch (JsonProcessingException e) {
            logger.error("解析微信API响应失败", e);
            throw new BusinessException("解析微信API响应失败: " + e.getMessage());
        } catch (Exception e) {
            logger.error("调用微信API获取AccessToken异常", e);
            if (e instanceof BusinessException) {
                throw e;
            }
            throw new BusinessException("调用微信API失败: " + e.getMessage());
        }
    }

    /**
     * 根据AccessToken和OpenId获取用户信息
     */
    @Override
    public WeChatUserInfoResponse getUserInfo(String accessToken, String openId) {
        try {
            String url = weChatConfig.getUserInfoUrl(accessToken, openId);
            logger.info("请求微信用户信息: {}", url.replace(accessToken, "***"));

            String response = restTemplate.getForObject(url, String.class);
            logger.debug("微信用户信息响应: {}", response);

            WeChatUserInfoResponse userInfoResponse = objectMapper.readValue(response, WeChatUserInfoResponse.class);

            if (!userInfoResponse.isSuccess()) {
                logger.error("获取微信用户信息失败: errcode={}, errmsg={}", 
                        userInfoResponse.getErrcode(), userInfoResponse.getErrmsg());
                throw new BusinessException("获取微信用户信息失败: " + userInfoResponse.getErrmsg());
            }

            return userInfoResponse;
        } catch (JsonProcessingException e) {
            logger.error("解析微信API响应失败", e);
            throw new BusinessException("解析微信API响应失败: " + e.getMessage());
        } catch (Exception e) {
            logger.error("调用微信API获取用户信息异常", e);
            if (e instanceof BusinessException) {
                throw e;
            }
            throw new BusinessException("调用微信API失败: " + e.getMessage());
        }
    }

    /**
     * 获取微信扫码登录二维码图片URL
     */
    @Override
    public String getQrCodeImageUrl(String redirectUri, String state) {
        // 生成微信网页授权二维码URL
        String qrCodeUrl = weChatConfig.getQrCodeUrl(redirectUri, state);
        // 转换为二维码图片URL
        return weChatConfig.getQrCodeImageUrl(qrCodeUrl);
    }
}

