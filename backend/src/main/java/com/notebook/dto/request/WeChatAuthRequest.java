package com.notebook.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * 微信认证请求对象
 */
public class WeChatAuthRequest {

    @NotBlank(message = "授权码不能为空")
    private String code;

    private String state;

    // getter和setter方法
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}