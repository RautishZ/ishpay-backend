package com.ishpay.ishpay.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.ishpay.ishpay.pojo.LoginPojo;
import com.ishpay.ishpay.services.UserServices;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserController {

    @Autowired
    private UserServices userServices;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginPojo loginPojo) {
        return userServices.register(loginPojo);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginPojo loginPojo, HttpServletRequest request) {

        return userServices.login(loginPojo, request);
    }

}
