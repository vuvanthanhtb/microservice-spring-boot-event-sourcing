package com.thanhvv.notification.event;

import com.thanhvv.common.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class EventConsumer {
    @Autowired
    private EmailService emailService;

    @RetryableTopic(
            attempts = "4", // 3 topic retry + 1 topic DLQ
            backoff = @Backoff(delay = 1000, multiplier = 2),
            autoCreateTopics = "true",
            dltStrategy = DltStrategy.FAIL_ON_ERROR,
            include = {
                    ReflectiveOperationException.class,
                    RuntimeException.class
            }
    )
    @KafkaListener(topics = "test", containerFactory = "kafkaListenerContainerFactory")
    public void listen(String message) {
        log.info("Received message: {}", message);
//        throw new RuntimeException("Error test");
    }

    @DltHandler
    void processDltMessage(@Payload String message) {
        log.info("DLT Received message: {}", message);
    }

    @KafkaListener(topics = "test-email", containerFactory = "kafkaListenerContainerFactory")
    public void testEmail(String message) {
        log.info("Received message:: {}", message);

        String template = "<div>" +
                "<h1>Welcome to %s</h1>" +
                "<p>Your username is:: <strong>%s</strong></p>" +
                "</div>";

        String filledTemplate = String.format(template, "Thanh", message);

        emailService.sendMail(message, "Thanks!!!", filledTemplate, true, null);
    }

    @KafkaListener(topics = "email-template", containerFactory = "kafkaListenerContainerFactory")
    public void emailTemplate(String message) {
        log.info("Received emailTemplate: {}", message);

        Map<String, Object> placeholders = new HashMap<>();
        placeholders.put("name", "Thanh");
        emailService.sendEmailWithTemplate(message, "Welcome to Thanh", "emailTemplate.ftl", placeholders, null);

    }
}
