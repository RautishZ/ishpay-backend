package com.ishpay.ishpay.pojo;

import java.util.HashSet;
import java.util.Set;

import com.ishpay.ishpay.entities.BeneficiaryEntity;
import com.ishpay.ishpay.entities.KycDocumentEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginPojo {
    String token;
    String email;
    String password;

    private String phoneNumber;

    private boolean isEmailVerified;

    private KycDocumentEntity kycDocuments;

    private Set<BeneficiaryEntity> beneficiaries;

    // Constructor with validation
    public LoginPojo(String token, String email, String password, String phoneNumber, boolean isEmailVerified,
            KycDocumentEntity kycDocuments, Set<BeneficiaryEntity> beneficiaries) {
        this.token = token != null ? token : "";
        this.email = email != null ? email : "";
        this.password = password != null ? password : "";
        this.phoneNumber = phoneNumber != null ? phoneNumber : "";
        this.isEmailVerified = isEmailVerified;
        this.kycDocuments = kycDocuments != null ? kycDocuments : new KycDocumentEntity();
        this.beneficiaries = beneficiaries != null ? beneficiaries : new HashSet<>();
    }
}
