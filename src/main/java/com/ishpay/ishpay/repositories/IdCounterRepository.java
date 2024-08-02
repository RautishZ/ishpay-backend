package com.ishpay.ishpay.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ishpay.ishpay.entities.IdCouterEntity;

public interface IdCounterRepository extends JpaRepository<IdCouterEntity, String> {

}
