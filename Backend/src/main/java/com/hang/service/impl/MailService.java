package com.hang.service.impl;

/**
 * @ClassName MailService
 * @Description TODO
 * @Author QiuLiHang
 * @DATE 2023/8/24 15:56
 * @Version 1.0
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.Date;

/**
 * 邮件业务类
 * @author qzz
 */
@Service
public class MailService {
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    /**
     * 注入邮件工具类
     */
    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Value("${spring.mail.username}")
    private String sendMailer;

    /**
     * 检测邮件信息类
     * @param to
     * @param subject
     * @param text
     */
    private void checkMail(String to,String subject,String text){
        if(to.isEmpty()){
            throw new RuntimeException("邮件收信人不能为空");
        }
        if(subject.isEmpty()){
            throw new RuntimeException("邮件主题不能为空");
        }
        if(text.isEmpty()){
            throw new RuntimeException("邮件内容不能为空");
        }
    }

    /**
     * 发送纯文本邮件
     * @param to
     * @param subject
     * @param text
     */
    public void sendTextMailMessage(String to,String subject,String text){

        try {
            //true 代表支持复杂的类型
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage(),true);
            //邮件发信人
            mimeMessageHelper.setFrom(sendMailer);
            //邮件收信人  1或多个
            mimeMessageHelper.setTo(to.split(","));
            //邮件主题
            mimeMessageHelper.setSubject(subject);
            //邮件内容
            mimeMessageHelper.setText(text);
            //邮件发送时间
            mimeMessageHelper.setSentDate(new Date());

            //发送邮件
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
            System.out.println("发送邮件成功："+sendMailer+"->"+to);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("发送邮件失败："+e.getMessage());
        }
    }
}

