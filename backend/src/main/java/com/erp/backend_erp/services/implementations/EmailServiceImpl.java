package com.erp.backend_erp.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.erp.backend_erp.dto.emails.EmailRequestDto;
import com.erp.backend_erp.services.EmailService;
import com.erp.backend_erp.util.HtmlTemplateUtil;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
    
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void enviarCorreo(EmailRequestDto request) throws Exception {
        String html = HtmlTemplateUtil.cargarPlantilla(request.getTemplate(), request.getVariables());

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setFrom("contacto@cloudtechnological.shop");
        helper.setTo(request.getTo());
        helper.setSubject(request.getSubject());
        helper.setText(html, true); // true = HTML

        mailSender.send(mimeMessage);
    }
}
