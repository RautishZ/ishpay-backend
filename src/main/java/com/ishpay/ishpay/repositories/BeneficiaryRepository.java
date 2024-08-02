package com.ishpay.ishpay.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ishpay.ishpay.entities.BeneficiaryEntity;
import com.ishpay.ishpay.entities.UserEntity;

@Repository
public interface BeneficiaryRepository extends JpaRepository<BeneficiaryEntity, String> {

    boolean existsByUserAndAccountNumberAndBankName(UserEntity user, String accountNumber, String ifscCode);

}
