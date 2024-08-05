package com.ishpay.ishpay.services;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ishpay.ishpay.entities.UserEntity;
import com.ishpay.ishpay.entities.VerificationTokenEntity;
import com.ishpay.ishpay.repositories.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> sendVerificationEmail(UserEntity user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        try {
            String token = UUID.randomUUID().toString();
            VerificationTokenEntity verificationToken = user.getVerificationToken();
            if (verificationToken == null) {
                verificationToken = new VerificationTokenEntity();
                verificationToken.setUser(user);
            }
            verificationToken.setToken(token);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.HOUR, 1); // 1 hour expiry
            verificationToken.setExpiryDate(calendar.getTime());

            user.setVerificationToken(verificationToken);

            userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating verification token.");
        }

        try {
            String verificationLink = "https://ishpay.com/verify?token=" + user.getVerificationToken().getToken();

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("no-reply.ishpay@hotmail.com");
            helper.setTo(user.getEmail());
            helper.setSubject("Verify Your Email Address for IshPay");
            helper.setText(buildEmailContent(verificationLink), true);
            javaMailSender.send(message);

            return ResponseEntity.ok("Email sent");
        } catch (MailException | MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send verification email.");
        }
    }

    private String buildEmailContent(String verificationLink) {
        return "<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>Email Verification</title>" +
                "</head>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; color: #333; margin: 0; padding: 0;'>"
                +
                "<div style='width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffe0cc; border-radius: 15px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); border: 2px solid #ad1e23;'>"
                +
                "<div style='background-color: #ad1e23; color: white; text-align: center; padding: 20px; border-radius: 14px 14px 0 0;'>"
                +
                "<h1 style='margin: 0;'>Welcome to IshPay!</h1>" +
                "</div>" +
                "<div style='padding: 20px; text-align: center;'>" +
                "<p style='margin: 0 0 10px;'>Dear User,</p>" +
                "<p style='margin: 0 0 20px;'>Thank you for signing up with IshPay! To complete your registration, please verify your email address by clicking the button below:</p>"
                +
                "<a href='" + verificationLink
                + "' style='display: inline-block; padding: 15px 20px; font-size: 16px; color: white; background-color: #ad1e23; text-decoration: none; border-radius: 5px;'>Confirm Your Email Address</a>"
                +
                "<p style='margin: 20px 0 0;'>If you did not create an account, please ignore this email.</p>" +
                "</div>" +
                "<div style='background-color: #f9f9f9; color: #666; text-align: center; padding: 20px; font-size: 14px; border-radius: 0 0 15px 15px;'>"
                +
                "<p style='margin: 0 0 5px;'>At IshPay, our mission is to make managing your finances easy and stress-free. Whether you're transferring funds, paying bills, handling various financial tasks, or booking services, we’re here to help you every step of the way.</p>"
                +
                "<p style='margin: 5px 0;'><strong>Contact Us:</strong></p>" +
                "<p style='margin: 5px 0;'>Customer Service: +91-8540891176 (10 AM to 5 PM)</p>" +
                "<p style='margin: 5px 0;'>Email: <a href='mailto:support@ishpay.com' style='color: #ad1e23;'>support@ishpay.com</a></p>"
                +
                "<p style='margin: 5px 0;'>Thank you for choosing IshPay!</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

}