package com.ims.msnotificationservice.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse {

    public ApiResponse() {

        requestRefID = "";
        responseCode = "";
        responseDesc = "";
        transactionID = "";
    }

    @JsonProperty("RequestRefID")
    private String requestRefID;

    @JsonProperty("ResponseCode")
    private String responseCode;

    @JsonProperty("ResponseDesc")
    private String responseDesc;

    @JsonProperty("TransactionID")
    private String transactionID;


    @Override
    public String toString() {

        return "{" +
                " RequestRefID='" + requestRefID + '\'' +
                ", ResponseCode='" + responseCode + '\'' +
                ", ResponseDesc='" + responseDesc + '\'' +
                ", TransactionID='" + transactionID + '\'' +
                '}';
    }
}
