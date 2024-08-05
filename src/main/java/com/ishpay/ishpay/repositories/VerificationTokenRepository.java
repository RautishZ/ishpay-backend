package com.ishpay.ishpay.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ishpay.ishpay.entities.VerificationTokenEntity;

public interface VerificationTokenRepository extends JpaRepository<VerificationTokenEntity, Long> {
    VerificationTokenEntity findByToken(String token);
}