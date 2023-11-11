package com.example.service;


public interface EmailService {

    /**
     * 发送邮件
     *
     * @param to
     * @param subject
     * @param content
     */
    void sendSimpleMail(String to, String subject, String content);


    /**
     * 发送html邮件
     * @param email 邮箱
     * @param captcha 验证码
     */
    void sendEmailMessage(String email,String captcha);
}