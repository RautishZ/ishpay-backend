package com.ishpay.ishpay.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.ishpay.ishpay.entities.UserEntity;
import com.ishpay.ishpay.pojo.LoginPojo;
import com.ishpay.ishpay.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtServices;
    @Autowired
    private EmailService emailServices;

    @Override
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public ResponseEntity<?> register(LoginPojo user) {
        // Check if user already exists
        if (userRepository.findByEmailIgnoreCase(user.getEmail().trim().toLowerCase()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("User already exists");
        }

        // Create and save the new user
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(user.getEmail().trim().toLowerCase());
        userEntity.setPassword(user.getPassword());
        userRepository.save(userEntity);

        // Generate token
        String token = jwtServices.generateToken(userEntity);
        user.setToken(token);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", token);

        emailServices.sendVerificationEmail(userEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    public ResponseEntity<?> login(LoginPojo loginPojo, HttpServletRequest request) {

        UserEntity user = jwtServices.getUser(request);

        if (user != null) {
            loginPojo.setPassword(null);
            loginPojo.setEmail(user.getEmail());
            loginPojo.setEmailVerified(user.getIsEmailVerified());
            loginPojo.setBeneficiaries(user.getBeneficiaries());
            loginPojo.setKycDocuments(user.getKycDocuments());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(loginPojo);
        }

        // Check if the email exists and is active
        UserEntity userEntity = userRepository.findByEmailIgnoreCase(loginPojo.getEmail()).orElse(null);

        if (userEntity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        if (!userEntity.getIsActive()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Account blocked");
        }

        // Verify password using PasswordEncoder
        if (loginPojo.getPassword().equals(userEntity.getPassword())) {
            loginPojo.setPassword(null);
            loginPojo.setEmail(userEntity.getEmail());
            loginPojo.setEmailVerified(userEntity.getIsEmailVerified());
            loginPojo.setBeneficiaries(userEntity.getBeneficiaries());
            loginPojo.setKycDocuments(userEntity.getKycDocuments());

            String token = jwtServices.generateToken(userEntity);
            loginPojo.setToken(token);
            return ResponseEntity.ok().body(loginPojo);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Wrong password");
    }

    public ResponseEntity<?> resendEmail(HttpServletRequest request) {
        UserEntity user = jwtServices.getUser(request);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized");
        }

        return emailServices.sendVerificationEmail(user);
    }

}
