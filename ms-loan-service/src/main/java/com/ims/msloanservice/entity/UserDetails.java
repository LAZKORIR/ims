package com.ims.msloanservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_users")
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_ref_id")
    private String requestRefID;

    @Column(name = "first_name")
    private String firstname;

    @Column(name = "last_name")
    private String lastname;

    @Column(name = "source_system")
    private String sourceSystem;

    @Column(name = "msisdn")
    private String msisdn;

    @Column(name = "email")
    private String email;

    @Column(name = "wallet_amount")
    private BigDecimal walletAmount;
}
