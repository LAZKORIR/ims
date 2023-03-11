package com.ims.apigateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepayLoanDetails implements Serializable {
    private String id;
    private String requestRefID;
    private String sourceSystem;
    private String msisdn;
    private BigDecimal amount;
}
