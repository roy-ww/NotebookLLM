package com.notebook.controller;

import com.notebook.dto.request.UpdateUserInfoRequest;
import com.notebook.dto.response.ApiResponse;
import com.notebook.dto.response.UserInfoResponse;
import com.notebook.service.UserService;
import com.notebook.util.JwtUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * 用户控制器
 * 处理用户信息相关的HTTP请求
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 获取当前用户信息
     * GET /api/user/me
     */
    @GetMapping("/me")
    public ApiResponse<UserInfoResponse> getCurrentUserInfo(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        try {
            // 从Authorization header中提取token
            String token = extractTokenFromHeader(authorization);
            if (token == null) {
                return ApiResponse.error("未提供认证令牌");
            }

            // 验证token
            if (!jwtUtil.validateToken(token)) {
                return ApiResponse.error("认证令牌无效或已过期");
            }

            // 从token中获取用户ID
            Long userId = jwtUtil.getUserIdFromToken(token);
            Optional<UserInfoResponse> userInfoOptional = userService.getUserInfoById(userId);

            if (userInfoOptional.isEmpty()) {
                return ApiResponse.error("用户不存在");
            }

            logger.info("获取用户信息成功: userId={}", userId);
            return ApiResponse.success("获取用户信息成功", userInfoOptional.get());
        } catch (Exception e) {
            logger.error("获取用户信息失败", e);
            return ApiResponse.error("获取用户信息失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取用户信息
     * GET /api/user/{userId}
     */
    @GetMapping("/{userId}")
    public ApiResponse<UserInfoResponse> getUserInfoById(@PathVariable Long userId) {
        try {
            Optional<UserInfoResponse> userInfoOptional = userService.getUserInfoById(userId);

            if (userInfoOptional.isEmpty()) {
                return ApiResponse.error("用户不存在");
            }

            logger.info("获取用户信息成功: userId={}", userId);
            return ApiResponse.success("获取用户信息成功", userInfoOptional.get());
        } catch (Exception e) {
            logger.error("获取用户信息失败: userId={}", userId, e);
            return ApiResponse.error("获取用户信息失败: " + e.getMessage());
        }
    }

    /**
     * 更新用户信息
     * PUT /api/user/me
     */
    @PutMapping("/me")
    public ApiResponse<UserInfoResponse> updateUserInfo(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Valid @RequestBody UpdateUserInfoRequest updateRequest) {
        try {
            // 从Authorization header中提取token
            String token = extractTokenFromHeader(authorization);
            if (token == null) {
                return ApiResponse.error("未提供认证令牌");
            }

            // 验证token
            if (!jwtUtil.validateToken(token)) {
                return ApiResponse.error("认证令牌无效或已过期");
            }

            // 从token中获取用户ID
            Long userId = jwtUtil.getUserIdFromToken(token);
            Optional<UserInfoResponse> updatedUserInfoOptional = 
                    userService.updateUserInfo(userId, updateRequest);

            if (updatedUserInfoOptional.isEmpty()) {
                return ApiResponse.error("用户不存在");
            }

            logger.info("更新用户信息成功: userId={}", userId);
            return ApiResponse.success("更新用户信息成功", updatedUserInfoOptional.get());
        } catch (Exception e) {
            logger.error("更新用户信息失败", e);
            return ApiResponse.error("更新用户信息失败: " + e.getMessage());
        }
    }

    /**
     * 从Authorization header中提取token
     */
    private String extractTokenFromHeader(String authorization) {
        if (authorization == null || authorization.trim().isEmpty()) {
            return null;
        }

        // Authorization header格式: "Bearer {token}"
        if (authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }

        return authorization;
    }
}

