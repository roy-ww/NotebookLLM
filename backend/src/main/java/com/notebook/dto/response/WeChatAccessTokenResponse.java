package com.notebook.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 微信AccessToken响应对象
 */
public class WeChatAccessTokenResponse {

    /**
     * 访问令牌
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * 过期时间（秒）
     */
    @JsonProperty("expires_in")
    private Integer expiresIn;

    /**
     * 刷新令牌
     */
    @JsonProperty("refresh_token")
    private String refreshToken;

    /**
     * 用户OpenID
     */
    @JsonProperty("openid")
    private String openid;

    /**
     * 用户UnionID
     */
    @JsonProperty("unionid")
    private String unionid;

    /**
     * 作用域
     */
    @JsonProperty("scope")
    private String scope;

    /**
     * 错误码
     */
    @JsonProperty("errcode")
    private Integer errcode;

    /**
     * 错误信息
     */
    @JsonProperty("errmsg")
    private String errmsg;

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return errcode == null || errcode == 0;
    }

    // Getter and Setter methods
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}

