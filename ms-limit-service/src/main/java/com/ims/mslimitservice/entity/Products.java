package com.ims.mslimitservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_products")
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "max_limit")
    private BigDecimal maxLimit;

    @Column(name = "percentage_interest")
    private double percentageInterest;

    @Column(name = "tenure_days")
    private Integer tenureDays;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "mode")
    private String mode;
}
