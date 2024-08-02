package com.ishpay.ishpay.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ishpay.ishpay.entities.BeneficiaryEntity;
import com.ishpay.ishpay.entities.UserEntity;
import com.ishpay.ishpay.repositories.BeneficiaryRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class BeneficiaryServices {

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Autowired
    private JwtServices jwtServices;

    public String addBeneficiary(BeneficiaryEntity beneficiaryEntity, HttpServletRequest request) {
        UserEntity user = jwtServices.getUser(request);

        boolean beneficiaryExists = beneficiaryRepository.existsByUserAndAccountNumberAndBankName(
                user,
                beneficiaryEntity.getAccountNumber().trim(),
                beneficiaryEntity.getIfscCode().trim());

        if (beneficiaryExists) {
            return "Beneficiary already exists";
        }

        beneficiaryEntity.setUser(user);
        beneficiaryRepository.save(beneficiaryEntity);
        return "Added Successfully";
    }
}
