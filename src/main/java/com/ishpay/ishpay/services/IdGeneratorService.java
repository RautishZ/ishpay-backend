package com.ishpay.ishpay.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ishpay.ishpay.entities.IdCouterEntity;
import com.ishpay.ishpay.repositories.IdCounterRepository;

@Service
public class IdGeneratorService {

    @Autowired
    private IdCounterRepository idCounterRepository;

    public String generateId(String prefix) {

        IdCouterEntity counterEntity = idCounterRepository.findById(prefix).orElseGet(() -> {
            IdCouterEntity newCounterEntity = new IdCouterEntity();
            newCounterEntity.setPrefix(prefix);
            newCounterEntity.setCounter(0L);
            return newCounterEntity;
        });

        // Increment the counter
        Long newCounterValue = counterEntity.getCounter() + 1;
        counterEntity.setCounter(newCounterValue);

        // Save the updated counter value
        idCounterRepository.save(counterEntity);

        // Return the generated ID
        return prefix + String.format("%04d", newCounterValue);
    }
}
