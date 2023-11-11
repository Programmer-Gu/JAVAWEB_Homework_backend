package com.example.service.Impl;

import com.example.common.Result;
import com.example.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    // JavaMailSender 在Mail 自动配置类 MailSenderAutoConfiguration 中已经导入，这里直接注入使用即可
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender; //发送者邮箱

    /**
     * 发送邮件方法
     *
     * @param to      接收者邮箱
     * @param subject 主题
     * @param content 内容
     */
    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage simpMsg = new SimpleMailMessage();
        simpMsg.setFrom(sender);
        simpMsg.setTo(to);
        simpMsg.setSubject(subject);
        simpMsg.setText(content);
        //发送邮件
        javaMailSender.send(simpMsg);
        log.info("邮件发送成功");
    }


    /**
     * 读取邮件模板
     * 替换模板中的信息
     *
     * @param captcha 验证码
     * @return
     */
    public String buildContent(String captcha) {
//        // 创建引擎
//        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("email", TemplateConfig.ResourceMode.CLASSPATH));
//        // 加载模板
//        Template template = engine.getTemplate("验证码.ftl");
//        log.info("读取邮件模板成功");
//        return template.render(Dict.create().set("code", captcha));
        return null;
    }


    /**
     * 向用户邮箱发送短信
     *
     * @param email 收件人邮箱
     */
    public void sendEmailMessage(String email,String captcha) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            //邮箱发送内容组成
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject("主题");
            helper.setText(buildContent(captcha + ""), true);
            helper.setTo(email);
            helper.setFrom(sender);
            javaMailSender.send(message);
            log.info("邮件发送成功");
        } catch (jakarta.mail.MessagingException e) {
            e.printStackTrace();
        }
    }


}
