package com.ishpay.ishpay.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.ishpay.ishpay.entities.UserEntity;
import com.ishpay.ishpay.entities.VerificationTokenEntity;
import com.ishpay.ishpay.pojo.LoginPojo;
import com.ishpay.ishpay.repositories.UserRepository;
import com.ishpay.ishpay.repositories.VerificationTokenRepository;
import com.ishpay.ishpay.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class UserController {

    @Autowired
    private UserService userServices;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginPojo loginPojo) {
        return userServices.register(loginPojo);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginPojo loginPojo, HttpServletRequest request) {

        return userServices.login(loginPojo, request);
    }

    @PostMapping("/resend-email")
    public ResponseEntity<?> resendEmail(HttpServletRequest request) {
        return userServices.resendEmail(request);
    }

    @Transactional
    @GetMapping("/verify")
    public String verifyAccount(@RequestParam String token) {

        VerificationTokenEntity verificationToken = verificationTokenRepository.findByToken(token);

        if (verificationToken == null) {
            return "Invalid token";
        }

        UserEntity user = verificationToken.getUser();
        if (user.getIsEmailVerified()) {
            return "Email is already verified";
        }

        Calendar calendar = Calendar.getInstance();

        if ((verificationToken.getExpiryDate().getTime() - calendar.getTime().getTime()) <= 0) {
            return "Token expired";
        }

        user.setIsEmailVerified(true);

        userRepository.save(user);

        return "Email verified successfully";
    }

}
