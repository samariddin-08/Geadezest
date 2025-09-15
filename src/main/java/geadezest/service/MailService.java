package geadezest.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service

public class MailService {

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;

    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String toEmail, String title, Object message) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(fromEmail);
        mailMessage.setTo(toEmail);
        mailMessage.setSubject(title);
        mailMessage.setText(message.toString());
        javaMailSender.send(mailMessage);
        
    }
}
