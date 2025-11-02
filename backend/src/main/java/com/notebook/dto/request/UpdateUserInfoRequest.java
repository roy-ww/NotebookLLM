package com.notebook.dto.request;

import jakarta.validation.constraints.Size;

/**
 * 更新用户信息请求对象
 */
public class UpdateUserInfoRequest {

    @Size(max = 100, message = "昵称长度不能超过100个字符")
    private String nickname;

    // getter和setter方法
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}