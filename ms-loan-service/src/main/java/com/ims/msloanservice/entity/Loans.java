package com.ims.msloanservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_loans")
public class Loans {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private String productID;

    @Column(name = "request_ref_id")
    private String referenceID;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "msisdn")
    private String msisdn;

    @Column(name = "status")
    private String status;

    @Column(name = "user_id")
    private Integer userid;

    @OneToOne
    @JoinColumn(name = "user_id",insertable = false,updatable = false)
    private UserDetails  userDetails;


}
