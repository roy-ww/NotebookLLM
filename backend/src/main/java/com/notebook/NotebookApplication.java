package com.notebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Notebook LLM 主启动类
 * 
 * 智能学习笔记系统，提供微信登录注册等功能
 */
@SpringBootApplication
public class NotebookApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotebookApplication.class, args);
    }

}