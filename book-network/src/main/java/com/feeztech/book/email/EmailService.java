package com.feeztech.book.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine springTemplateEngine;

    /*@Async
    public void sendEmail(String to, String username, EmailTemplateName emailTemplateName, String confirmationUrl, String activationCode, String subject) throws MessagingException {

        String templateName;
        if (emailTemplateName == null) {
            templateName = "confirm-email";
        } else {
            templateName = emailTemplateName.getName();
        }

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MULTIPART_MODE_MIXED, UTF_8.name());
        Map<String, Object> properties = new HashMap<>();
                properties.put("username", username);
                properties.put("confirmationUrl", confirmationUrl);
                properties.put("activation_code", activationCode);

                Context context = new Context();
                context.setVariables(properties);
                helper.setFrom("abdussallamabdulafeez39@gmail.com");
                helper.setTo(to);
                helper.setSubject(subject);

                String template = templateEngine.process(templateName, context);
                helper.setText(template, true);
                mailSender.send(mimeMessage);

    }*/

    public void sendVerificationEmail(
            String to,
            String username,
            String templateName,
            String activationUrl,
            String activationCode,
            String subject
    ) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("activation_code", activationCode);
        context.setVariable("confirmationUrl", activationUrl + "?token=" + activationCode);

        String htmlContent = springTemplateEngine.process("emails/" + templateName, context);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        emailSender.send(mimeMessage);
    }

    public void sendTestEmail() throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo("abdussalaamabdulafeez@gmail.com");
        helper.setSubject("Test Email");
        helper.setText("This is a test email from Spring Boot!", false);

        emailSender.send(mimeMessage);
    }

}
