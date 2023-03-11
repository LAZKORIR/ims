package com.ims.msnotificationservice.model;

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
    @JsonProperty("requestRefID")
    public String requestRefID;
    @NotNull(message = "The recipient cannot be null.")
    @NotBlank(message = "The recipient  value is required.")
    @JsonProperty("recipient")
    public String recipient;
    @JsonProperty("sourceSystem")
    public String sourceSystem;
    @JsonProperty("subject")
    public String subject;
    @JsonProperty("text")
    public String text;

}
