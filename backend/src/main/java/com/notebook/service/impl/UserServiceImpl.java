package com.notebook.service.impl;

import com.notebook.dto.request.UpdateUserInfoRequest;
import com.notebook.dto.response.UserInfoResponse;
import com.notebook.entity.User;
import com.notebook.exception.BusinessException;
import com.notebook.repository.UserRepository;
import com.notebook.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 用户服务实现类
 * 处理用户信息相关的业务逻辑
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    /**
     * 根据ID获取用户信息
     */
    @Override
    public Optional<UserInfoResponse> getUserInfoById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.map(UserInfoResponse::new);
    }

    /**
     * 根据用户名获取用户
     */
    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 根据微信OpenId获取用户
     */
    @Override
    public Optional<User> getUserByWechatOpenid(String wechatOpenid) {
        return userRepository.findByWechatOpenid(wechatOpenid);
    }

    /**
     * 创建新用户
     */
    @Override
    @Transactional
    public User createUser(User user) {
        // 验证用户名是否已存在
        if (user.getUsername() != null && existsByUsername(user.getUsername())) {
            throw new BusinessException("用户名已存在");
        }

        // 验证微信OpenId是否已存在
        if (user.getWechatOpenid() != null && existsByWechatOpenid(user.getWechatOpenid())) {
            throw new BusinessException("该微信账号已注册");
        }

        logger.info("创建新用户: username={}, nickname={}, openid={}", 
                user.getUsername(), user.getNickname(), user.getWechatOpenid());
        
        return userRepository.save(user);
    }

    /**
     * 更新用户信息
     */
    @Override
    @Transactional
    public Optional<UserInfoResponse> updateUserInfo(Long userId, UpdateUserInfoRequest updateRequest) {
        Optional<User> userOptional = userRepository.findById(userId);
        
        if (userOptional.isEmpty()) {
            return Optional.empty();
        }

        User user = userOptional.get();

        // 更新昵称
        if (updateRequest.getNickname() != null && !updateRequest.getNickname().trim().isEmpty()) {
            user.setNickname(updateRequest.getNickname().trim());
        }

        User updatedUser = userRepository.save(user);
        logger.info("更新用户信息: userId={}, nickname={}", userId, updatedUser.getNickname());

        return Optional.of(new UserInfoResponse(updatedUser));
    }

    /**
     * 检查用户名是否存在
     */
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * 检查微信OpenId是否存在
     */
    @Override
    public boolean existsByWechatOpenid(String wechatOpenid) {
        return userRepository.existsByWechatOpenid(wechatOpenid);
    }

    /**
     * 保存用户（用于更新已存在的用户）
     */
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}

