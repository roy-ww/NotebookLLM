package com.notebook.dto.response;

/**
 * 二维码扫码状态响应
 */
public class QrCodeStatusResponse {

    /**
     * 状态：pending-等待扫码，scanned-已扫码，confirmed-已确认，expired-已过期
     */
    private String status;

    /**
     * 授权码（当status为confirmed时返回）
     */
    private String code;

    /**
     * 消息描述
     */
    private String message;

    public QrCodeStatusResponse() {
    }

    public QrCodeStatusResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public QrCodeStatusResponse(String status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    // Getter and Setter methods
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

