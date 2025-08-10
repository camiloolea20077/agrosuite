package com.erp.backend_erp.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(System.getenv("EMAIL_HOST"));
        mailSender.setPort(Integer.parseInt(System.getenv("EMAIL_PORT")));
        mailSender.setUsername(System.getenv("EMAIL_USERNAME"));
        mailSender.setPassword(System.getenv("EMAIL_PASSWORD"));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");

        // usa TLS o SSL dependiendo del puerto
        if ("465".equals(System.getenv("EMAIL_PORT"))) {
            props.put("mail.smtp.ssl.enable", "true");
        } else {
            props.put("mail.smtp.starttls.enable", "true");
        }

        return mailSender;
    }
}
