package com.notebook.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 微信用户信息响应对象
 */
public class WeChatUserInfoResponse {

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
     * 用户昵称
     */
    @JsonProperty("nickname")
    private String nickname;

    /**
     * 用户头像URL
     */
    @JsonProperty("headimgurl")
    private String headimgurl;

    /**
     * 用户性别（0未知，1男，2女）
     */
    @JsonProperty("sex")
    private Integer sex;

    /**
     * 用户所在省份
     */
    @JsonProperty("province")
    private String province;

    /**
     * 用户所在城市
     */
    @JsonProperty("city")
    private String city;

    /**
     * 用户所在国家
     */
    @JsonProperty("country")
    private String country;

    /**
     * 用户特权信息
     */
    @JsonProperty("privilege")
    private String[] privilege;

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String[] getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String[] privilege) {
        this.privilege = privilege;
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

