package com.ishpay.ishpay.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ishpay.ishpay.entities.KycDocumentEntity;
import com.ishpay.ishpay.entities.UserEntity;
import com.ishpay.ishpay.repositories.KycDocumentRepository;
import com.ishpay.ishpay.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class KycDocumentServices {

    @Autowired
    private KycDocumentRepository kycDocumentRepository;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private IdGeneratorServices idGeneratorServices;
    @Autowired
    private JwtServices jwtServices;
    @Autowired
    private UserRepository userRepository;

    public String uploadKycDocument(String fullName, String panNumber, String aadhaarNumber, String dateOfBirth,
            String mobileNumber, MultipartFile selfie, MultipartFile aadhaarFile, MultipartFile panFile,
            HttpServletRequest request) {

        try {
            // Validate input
            if (fullName == null || panNumber == null || aadhaarNumber == null || dateOfBirth == null ||
                    mobileNumber == null || selfie == null || aadhaarFile == null || panFile == null) {
                return "Invalid input data.";
            }

            // Extract and validate JWT token
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                String usernameFromToken = jwtServices.extractUsername(token);
                UserEntity userFromToken = userRepository.findByEmailIgnoreCase(usernameFromToken)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

                // Create and save the KYC document
                KycDocumentEntity kycDocumentEntity = new KycDocumentEntity();
                kycDocumentEntity.setFullName(fullName);
                kycDocumentEntity.setPanNumber(panNumber);
                kycDocumentEntity.setAadhaarNumber(aadhaarNumber);

                // Ensure date format is compatible
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                kycDocumentEntity.setDob(LocalDate.parse(dateOfBirth, formatter));

                kycDocumentEntity.setMobileNumber(mobileNumber);

                String kycId = idGeneratorServices.generateId("KYC");

                kycDocumentEntity.setKycId(kycId);
                // Store files and get their paths
                String selfieFilename = fileStorageService.storeFile(kycId, selfie, "selfie");
                String idFileFilename = fileStorageService.storeFile(kycId, aadhaarFile, "AadhaarCard");
                String addressFileFilename = fileStorageService.storeFile(kycId, panFile, "PanCard");
                kycDocumentEntity.setSelfieUrl(selfieFilename);
                kycDocumentEntity.setIdFileUrl(idFileFilename);
                kycDocumentEntity.setAddressFileUrl(addressFileFilename);

                kycDocumentEntity.setUser(userFromToken);
                kycDocumentRepository.save(kycDocumentEntity);
                return "Identity verification successful!";
            } else {
                return "Invalid or missing token.";
            }

        } catch (Exception e) {

            return "There was an error processing your request.";
        }
    }
}
