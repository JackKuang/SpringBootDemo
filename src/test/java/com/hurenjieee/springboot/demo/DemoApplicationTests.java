package com.hurenjieee.springboot.demo;

import com.hurenjieee.springboot.demo.entity.User;
import com.hurenjieee.springboot.demo.mapper.UserMapper;
import com.hurenjieee.springboot.demo.service.IMailService;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@MapperScan("com.hurenjieee.springboot.demo.mapper")
@SpringBootTest
public class DemoApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    StringEncryptor encryptor;

    /**
     * 获取配置文件加密
     */
    @org.junit.Test
    public void getPass() {
        String url = encryptor.encrypt("jdbc:mysql://localhost:3306/demo?useUnicode=true&characterEncoding=utf8&useSSL=false");
        String name = encryptor.encrypt("root");
        String password = encryptor.encrypt("");
        String text = encryptor.encrypt("");
        System.out.println(url + "----------------");
        System.out.println(name + "----------------");
        System.out.println(password + "----------------");
        System.out.println(text + "----------------");
        Assert.assertTrue(name.length() > 0);
        Assert.assertTrue(password.length() > 0);
    }

    @Autowired
    private IMailService mailService;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testSimpleMail() throws Exception {
        mailService.sendSimpleMail("1092465834@qq.com","test simple mail","");
    }

    @Test
    public void testHtmlMail() throws Exception {
        String content="<html>\n" +
                "<body>\n" +
                "    <h3>hello world ! 这是一封html邮件!</h3>\n" +
                "</body>\n" +
                "</html>";
        mailService.sendHtmlMail("1092465834@qq.com","test simple mail",content);
    }

    @Test
    public void sendAttachmentsMail() {
        String filePath="D:\\log\\mail\\mail.log";
        mailService.sendAttachmentsMail("1092465834@qq.com", "主题：带附件的邮件", "有附件，请查收！", filePath);
    }


    @Test
    public void sendInlineResourceMail() {
        String rscId = "neo006";
        String content="<html><body>这是有图片的邮件：<img src=\'cid:" + rscId + "\' ></body></html>";
        String imgPath = "D:\\pic.png";
        mailService.sendInlineResourceMail("1092465834@qq.com", "主题：这是有图片的邮件", content, imgPath, rscId);
    }


    @Test
    public void sendTemplateMail() {
        //创建邮件正文
        Context context = new Context();
        context.setVariable("id", "006");
        String emailContent = templateEngine.process("emailTemplate", context);

        mailService.sendHtmlMail("1092465834@qq.com","主题：这是模板邮件",emailContent);
    }
}
