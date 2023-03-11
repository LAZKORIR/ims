package com.ims.msloanrepaymentservice.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse {

    public ApiResponse() {

        body = "";
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

    @JsonProperty("Body")
    private Object body;

    @Override
    public String toString() {

        String bodyString = body == null ? "" : body.toString();

        return "{" +
                " RequestRefID='" + requestRefID + '\'' +
                ", ResponseCode='" + responseCode + '\'' +
                ", ResponseDesc='" + responseDesc + '\'' +
                ", TransactionID='" + transactionID + '\'' +
                ", Parameters=" + bodyString +
                '}';
    }
}
