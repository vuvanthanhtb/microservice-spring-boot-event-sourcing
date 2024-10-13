package com.thanhvv.common.services;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Configuration config;

    /**
     * Send an email with optional HTML content and attachment.
     *
     * @param to         The recipient's email address.
     * @param subject    The subject of the email.
     * @param content    The body of the email, can be HTML or plain text.
     * @param isHtml     Whether the email body is HTML or plain text.
     * @param attachment An optional file attachment, can be null
     */
    public void sendMail(String to, String subject, String content, boolean isHtml, File attachment) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content, isHtml);

            if (attachment != null) {
                FileSystemResource file = new FileSystemResource(attachment);
                mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
            }

            mailSender.send(mimeMessage);
//            log.info("send email successfully to {}", to);
        } catch (MessagingException ex) {
//            log.error("Failed to send email {}", to, ex);
            // Handle the exception (retry logic, save to dlq...)
        }
    }

    /**
     * Send an email with optional HTML content and attachment.
     *
     * @param to           The recipient's email address.
     * @param subject      The subject of the email.
     * @param templateName The name of the HTML template file.
     * @param placeholders A map of placeholders and their replacements.
     * @param attachment   An optional file attachment, can be null
     */
    public void sendEmailWithTemplate(String to, String subject, String templateName, Map<String, Object> placeholders, File attachment) {
        try {
            Template template = config.getTemplate(templateName);
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, placeholders);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(html, true);

            if (attachment != null) {
                FileSystemResource file = new FileSystemResource(attachment);
                mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
            }

            mailSender.send(mimeMessage);
//            log.info("send email successfully to:: {}", to);
        } catch (MessagingException | IOException | TemplateException ex) {
//            log.error("Failed to send email {}", to, ex);
        }
    }
}
