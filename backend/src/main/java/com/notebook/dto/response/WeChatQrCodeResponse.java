package com.notebook.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 微信二维码响应对象
 */
@Data
public class WeChatQrCodeResponse {

    /**
     * 二维码ticket
     */
    @JsonProperty("ticket")
    private String ticket;

    /**
     * 二维码图片URL
     */
    @JsonProperty("qrcode_url")
    private String qrcodeUrl;

    /**
     * 过期时间（秒）
     */
    @JsonProperty("expire_seconds")
    private Integer expireSeconds;

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
}

