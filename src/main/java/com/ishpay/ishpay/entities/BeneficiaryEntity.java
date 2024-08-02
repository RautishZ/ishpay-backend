package com.ishpay.ishpay.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users_beneficiaries")
public class BeneficiaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String beneficiaryID;
    @Column(nullable = false)
    private String beneficiaryName;
    @Column(nullable = false)
    private String accountNumber;
    @Column(nullable = false)
    private String ifscCode;
    @Column(nullable = false)
    private String bankName;
    private BeneficiaryStatus status = BeneficiaryStatus.PENDING;

    public enum BeneficiaryStatus {
        PENDING, VERIFIED, REJECTED;
    }

    @JsonIgnore
    @ManyToOne
    UserEntity user;

}
