package com.ims.mslimitservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiRequest {

    @NotNull(message = "The Request Ref ID cannot be null.")
    @NotBlank(message = "The Request Ref ID  value is required.")
    @JsonProperty("RequestRefID")
    public String requestRefID;
    @JsonProperty("firstname")
    public String firstname;
    @JsonProperty("lastname")
    public String lastname;
    @JsonProperty("SourceSystem")
    public String sourceSystem;
    @NotNull(message = "The msisdn cannot be null.")
    @NotBlank(message = "The msisdn  value is required.")
    @JsonProperty("msisdn")
    public String msisdn;
    @JsonProperty("email")
    public String email;

}
