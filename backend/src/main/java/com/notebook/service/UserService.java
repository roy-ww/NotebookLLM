package com.notebook.service;

import com.notebook.dto.request.UpdateUserInfoRequest;
import com.notebook.dto.response.UserInfoResponse;
import com.notebook.entity.User;

import java.util.Optional;

/**
 * 用户服务接口
 * 处理用户信息相关的业务逻辑
 */
public interface UserService {

    /**
     * 根据ID获取用户信息
     * @param userId 用户ID
     * @return 用户信息响应
     */
    Optional<UserInfoResponse> getUserInfoById(Long userId);

    /**
     * 根据用户名获取用户
     * @param username 用户名
     * @return 用户实体
     */
    Optional<User> getUserByUsername(String username);

    /**
     * 根据微信OpenId获取用户
     * @param wechatOpenid 微信OpenId
     * @return 用户实体
     */
    Optional<User> getUserByWechatOpenid(String wechatOpenid);

    /**
     * 创建新用户
     * @param user 用户实体
     * @return 创建的用户
     */
    User createUser(User user);

    /**
     * 更新用户信息
     * @param userId 用户ID
     * @param updateRequest 更新请求
     * @return 更新后的用户信息响应
     */
    Optional<UserInfoResponse> updateUserInfo(Long userId, UpdateUserInfoRequest updateRequest);

    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查微信OpenId是否存在
     * @param wechatOpenid 微信OpenId
     * @return 是否存在
     */
    boolean existsByWechatOpenid(String wechatOpenid);
}