package com.ims.msloanrepaymentservice.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDetails {
    private String requestRefID;
    private String sourceSystem;
    private String recipient;
    private String subject;
    private String text;
    private  String mode;
}
