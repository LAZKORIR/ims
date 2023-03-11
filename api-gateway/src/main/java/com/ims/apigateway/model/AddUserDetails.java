package com.ims.apigateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddUserDetails implements Serializable {

    private String requestRefID;
    private String firstname;
    private String lastname;
    private String sourceSystem;
    private String msisdn;
    private String email;
}
