package com.notebook.dto.response;

/**
 * 认证响应对象
 */
public class AuthResponse {

    private String token;
    private UserInfoResponse userInfo;

    public AuthResponse() {
    }

    public AuthResponse(String token, UserInfoResponse userInfo) {
        this.token = token;
        this.userInfo = userInfo;
    }

    // getter和setter方法
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserInfoResponse getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoResponse userInfo) {
        this.userInfo = userInfo;
    }
}