package com.notebook.service.impl;

import com.notebook.dto.request.WeChatAuthRequest;
import com.notebook.dto.response.AuthResponse;
import com.notebook.dto.response.UserInfoResponse;
import com.notebook.dto.response.WeChatAccessTokenResponse;
import com.notebook.dto.response.WeChatUserInfoResponse;
import com.notebook.entity.User;
import com.notebook.exception.BusinessException;
import com.notebook.repository.UserRepository;
import com.notebook.service.AuthService;
import com.notebook.service.UserService;
import com.notebook.service.WeChatService;
import com.notebook.util.JwtUtil;
import com.notebook.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * 认证服务实现类
 * 处理微信登录注册相关的业务逻辑
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    /**
     * 处理微信登录
     */
    @Override
    @Transactional
    public AuthResponse processWeChatLogin(WeChatAuthRequest request) {
        logger.info("处理微信登录请求: code={}", request.getCode());

        // 1. 通过授权码获取微信AccessToken
        WeChatAccessTokenResponse tokenResponse = 
                weChatService.getAccessTokenByCode(request.getCode());

        // 2. 通过AccessToken获取用户信息
        WeChatUserInfoResponse weChatUserInfo = 
                weChatService.getUserInfo(
                        tokenResponse.getAccessToken(), 
                        tokenResponse.getOpenid()
                );

        // 3. 查找或创建用户
        User user = findOrCreateUser(weChatUserInfo, tokenResponse);

        // 4. 生成JWT Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        // 5. 返回认证响应
        return new AuthResponse(token, new UserInfoResponse(user));
    }

    /**
     * 获取微信授权URL
     */
    @Override
    public String getWeChatAuthUrl(String redirectUri) {
        return weChatService.getWeChatAuthUrl(redirectUri);
    }

    /**
     * 处理微信授权回调
     */
    @Override
    @Transactional
    public AuthResponse handleWeChatCallback(String code, String state) {
        logger.info("处理微信授权回调: code={}, state={}", code, state);

        WeChatAuthRequest request = new WeChatAuthRequest();
        request.setCode(code);
        request.setState(state);

        return processWeChatLogin(request);
    }

    /**
     * 获取微信扫码登录二维码图片URL
     */
    @Override
    public String getWeChatQrCodeImageUrl(String redirectUri, String state) {
        return weChatService.getQrCodeImageUrl(redirectUri, state);
    }

    /**
     * 查找或创建用户
     */
    private User findOrCreateUser(WeChatUserInfoResponse weChatUserInfo, WeChatAccessTokenResponse tokenResponse) {
        String openId = weChatUserInfo.getOpenid();
        
        // 先根据OpenId查找用户
        Optional<User> userOptional = userService.getUserByWechatOpenid(openId);

        if (userOptional.isPresent()) {
            // 用户已存在，更新用户信息
            User user = userOptional.get();
            
            // 更新微信用户信息（头像、昵称可能变化）
            if (weChatUserInfo.getNickname() != null) {
                user.setNickname(weChatUserInfo.getNickname());
            }
            if (weChatUserInfo.getHeadimgurl() != null) {
                user.setAvatarUrl(weChatUserInfo.getHeadimgurl());
            }
            if (tokenResponse.getUnionid() != null) {
                user.setWechatUnionid(tokenResponse.getUnionid());
            }
            user.setUpdatedAt(LocalDateTime.now());

            logger.info("更新已存在用户: userId={}, openid={}", user.getId(), openId);
            // 直接保存更新后的用户，不需要调用createUser
            return userRepository.save(user);
        } else {
            // 用户不存在，创建新用户
            User newUser = new User();
            
            // 生成唯一的用户名（基于OpenId或UUID）
            String username = "wx_" + openId.substring(0, Math.min(8, openId.length())) + 
                             "_" + UUID.randomUUID().toString().substring(0, 6);
            
            // 确保用户名唯一
            while (userService.existsByUsername(username)) {
                username = "wx_" + openId.substring(0, Math.min(8, openId.length())) + 
                          "_" + UUID.randomUUID().toString().substring(0, 6);
            }

            newUser.setUsername(username);
            newUser.setNickname(weChatUserInfo.getNickname() != null ? 
                    weChatUserInfo.getNickname() : "微信用户");
            newUser.setPassword(PasswordUtil.encodePassword(UUID.randomUUID().toString())); // 随机密码，微信登录不需要密码
            newUser.setWechatOpenid(openId);
            
            if (tokenResponse.getUnionid() != null) {
                newUser.setWechatUnionid(tokenResponse.getUnionid());
            }
            
            if (weChatUserInfo.getHeadimgurl() != null) {
                newUser.setAvatarUrl(weChatUserInfo.getHeadimgurl());
            }

            logger.info("创建新用户: username={}, openid={}", username, openId);
            return userService.createUser(newUser);
        }
    }
}

