package com.ishpay.ishpay.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ishpay.ishpay.entities.KycDocumentEntity;

public interface KycDocumentRepository extends JpaRepository<KycDocumentEntity, String> {

}
