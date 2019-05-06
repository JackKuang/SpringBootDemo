package com.hurenjieee.springboot.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.hurenjieee.springboot.demo.service.IMailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * @author Jack
 * @date 2019/3/7 10:40
 */
@Service
public class MailServiceImpl implements IMailService {

    private Marker mailMarker = MarkerManager.getMarker("MAIL");
    private Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.fromMail.addr}")
    private String from;

    @Override
    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendHtmlMail(String to, String subject, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            buildHelper(message, to, subject, content);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendAttachmentsMail(String to, String subject, String content, String filePath) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = buildHelper(message, to, subject, content);
            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            helper.addAttachment(fileName, file);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendInlineResourceMail(String to, String subject, String content, String rscPath, String rscId) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = buildHelper(message, to, subject, content);
            FileSystemResource res = new FileSystemResource(new File(rscPath));
            helper.addInline(rscId, res);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private MimeMessageHelper buildHelper(MimeMessage message, String to, String subject, String content) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        return helper;
    }

    @Override
    public void sendSimpleMailForException(String to, String subject, Throwable t) {
        ByteArrayOutputStream buf = new java.io.ByteArrayOutputStream();
        t.printStackTrace(new java.io.PrintWriter(buf, true));
        this.sendSimpleMail(to,"【demo异常邮件】", buf.toString());
    }
}
