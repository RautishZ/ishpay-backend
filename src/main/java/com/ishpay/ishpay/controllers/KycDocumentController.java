package com.ishpay.ishpay.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ishpay.ishpay.services.KycDocumentServices;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class KycDocumentController {

    @Autowired
    KycDocumentServices kycDocumentServices;

    @PostMapping("/verify-identity")
    public ResponseEntity<String> verifyIdentity(
            @RequestParam("fullName") String fullName,
            @RequestParam("panNumber") String panNumber,
            @RequestParam("aadhaarNumber") String aadhaarNumber,
            @RequestParam("dateOfBirth") String dateOfBirth,
            @RequestParam("mobileNumber") String mobileNumber,
            @RequestParam("selfie") MultipartFile selfie,
            @RequestParam("idFile") MultipartFile aadhaarFile,
            @RequestParam("addressFile") MultipartFile panFile,
            HttpServletRequest request) {

        // Log the received data for debugging
        System.out.println("Full Name: " + fullName);
        System.out.println("PAN Number: " + panNumber);
        System.out.println("Aadhaar Number: " + aadhaarNumber);
        System.out.println("Date of Birth: " + dateOfBirth);
        System.out.println("Mobile Number: " + mobileNumber);

        // Call the service to handle the files and data
        String result = kycDocumentServices.uploadKycDocument(fullName, panNumber, aadhaarNumber, dateOfBirth,
                mobileNumber,
                selfie, aadhaarFile, panFile, request);

        return ResponseEntity.ok(result);
    }
}
