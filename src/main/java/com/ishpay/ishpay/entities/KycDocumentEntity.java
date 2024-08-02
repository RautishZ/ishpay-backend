package com.ishpay.ishpay.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "kyc_documents")
public class KycDocumentEntity {
    @Id
    private String kycId;

    private String fullName;

    @Column(unique = true)
    private String aadhaarNumber;

    @Column(unique = true)
    private String panNumber;

    private LocalDate dob;

    private String mobileNumber;

    @Column(name = "selfie_url")
    private String selfieUrl;

    @Column(name = "id_file_url")
    private String idFileUrl;

    @Column(name = "address_file_url")
    private String addressFileUrl;

    private KycStatus status = KycStatus.PENDING;
    @OneToOne

    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public enum KycStatus {
        PENDING, VERIFIED, REJECTED;
    }
}
