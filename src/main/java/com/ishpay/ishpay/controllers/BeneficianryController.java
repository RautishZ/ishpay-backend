package com.ishpay.ishpay.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ishpay.ishpay.entities.BeneficiaryEntity;
import com.ishpay.ishpay.services.BeneficiaryService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class BeneficianryController {

    @Autowired
    private BeneficiaryService beneficiaryServices;

    @PostMapping("/beneficiary")
    public String addBeneficiary(@RequestBody BeneficiaryEntity beneficiaryEntity, HttpServletRequest request) {
        System.out.println("Beneficiary Name: " + beneficiaryEntity.getAccountNumber());
        return beneficiaryServices.addBeneficiary(beneficiaryEntity, request);
    }

}
