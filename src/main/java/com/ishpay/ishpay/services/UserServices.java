package com.ishpay.ishpay.services;

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
public class UserServices implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtServices jwtServices;

    @Override
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public ResponseEntity<LoginPojo> register(LoginPojo user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());
        userRepository.save(userEntity);
        String token = jwtServices.generateToken(userEntity);
        user.setToken(token);
        return ResponseEntity.ok().body(user);

    }

    public ResponseEntity<?> login(LoginPojo loginPojo, HttpServletRequest request) {

        UserEntity user = jwtServices.getUser(request);
        if (user != null) {
            loginPojo.setPassword(null);

            loginPojo.setEmail(user.getEmail());
            loginPojo.setEmailVerified(user.isEmailVerified());
            loginPojo.setBeneficiaries(user.getBeneficiaries());
            loginPojo.setKycDocuments(user.getKycDocuments());
            return ResponseEntity.status(HttpStatus.OK)
                    .body(loginPojo);
        }

        user = userRepository.findByEmailIgnoreCase(loginPojo.getEmail())
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        if (loginPojo.getPassword().equals(user.getPassword())) {

            loginPojo.setPassword(null);
            loginPojo.setEmail(user.getEmail());
            loginPojo.setEmailVerified(user.isEmailVerified());
            loginPojo.setBeneficiaries(user.getBeneficiaries());
            loginPojo.setKycDocuments(user.getKycDocuments());

            String token = jwtServices.generateToken(user);
            loginPojo.setToken(token);
            return ResponseEntity.ok().body(loginPojo);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Wrong password");
    }

}
