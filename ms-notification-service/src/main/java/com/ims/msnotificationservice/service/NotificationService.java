package com.ims.msnotificationservice.service;

import com.ims.msnotificationservice.configs.ConfigProperties;
import com.ims.msnotificationservice.dto.ApiRequest;
import com.ims.msnotificationservice.dto.ApiResponse;
import com.ims.msnotificationservice.utils.LogHelper;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static com.twilio.example.Example.ACCOUNT_SID;
import static com.twilio.example.Example.AUTH_TOKEN;

@Service
@Slf4j
public class NotificationService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    ConfigProperties configProperties;

    final LogHelper logHelper = LogHelper.withInitializer(log, (builder) ->
            builder
                    .operationName("Notification Service")
                    .targetSystem("Users"));

    /**
     * Send Email function
     * handles requests accessed direct from http request
     * @param apiRequest
     * @return
     */
    public Mono<ResponseEntity<ApiResponse>> sendEmail(ApiRequest apiRequest) {

        String recipient = apiRequest.getRecipient();
        String refId = apiRequest.getRequestRefID();
        String subject = apiRequest.getSubject();
        String text = apiRequest.getText();
        logHelper.build()
                .transactionID(refId)
                .logMsgType("Send Email")
                .logStatus("Processing")
                .logMsg("Email subject :  "+subject)
                .logDetailedMsg("Sending email to :  "+ recipient +" with text : "+text)
                .info();
        ApiResponse apiResponse = new ApiResponse();
        String requestRefid = String.valueOf(UUID.randomUUID());
        HttpStatus status ;
        try {

            senMail(recipient,subject,text);

            logHelper.build()
                    .transactionID(requestRefid)
                    .logMsgType("Email Sent")
                    .logStatus("Finished")
                    .logMsg("Email subject :  " + subject)
                    .logDetailedMsg("Email sent to :  " + recipient + " with text : " + text)
                    .info();
            status=HttpStatus.OK;
            apiResponse.setResponseCode("200");
            apiResponse.setRequestRefID(requestRefid);
            apiResponse.setResponseDesc("Email Sent Success");
        }catch (Exception ex){
            logHelper.build()
                    .transactionID(requestRefid)
                    .logMsgType("Email Sent")
                    .logStatus("Finished")
                    .logMsg("Failed")
                    .logDetailedMsg("Email Failed to sent with error: "+ex.getMessage())
                    .info();
            status=HttpStatus.INTERNAL_SERVER_ERROR;
            apiResponse.setResponseCode("500");
            apiResponse.setRequestRefID(requestRefid);
            apiResponse.setResponseDesc("Email Sending Failed");
        }

        return  Mono.just(ResponseEntity.status(status)
                .body(apiResponse));
    }


    /**
     * Send sms function
     * handles requests accessed direct from http request
     * @param apiRequest
     * @return
     */
    public Mono<ResponseEntity<ApiResponse>> sendSms(ApiRequest apiRequest) {

        String recipient = apiRequest.getRecipient();
        String refId = apiRequest.getRequestRefID();
        String text = apiRequest.getText();
        logHelper.build()
                .transactionID(refId)
                .logMsgType("Send Sms")
                .logStatus("Processing")
                .logMsg("Sending  Sms:  ")
                .logDetailedMsg("Sending Sms to :  "+ recipient +" with text : "+text)
                .info();
        ApiResponse apiResponse = new ApiResponse();
        String requestRefid = String.valueOf(UUID.randomUUID());
        HttpStatus status ;
        try {

            sendSms(recipient,text);

            logHelper.build()
                    .transactionID(requestRefid)
                    .logMsgType("Sms Sent")
                    .logStatus("Finished")
                    .logMsg("Sms Sending :  " )
                    .logDetailedMsg("Sms sent to :  " + recipient + " with text : " + text)
                    .info();
            status=HttpStatus.OK;
            apiResponse.setResponseCode("200");
            apiResponse.setRequestRefID(requestRefid);
            apiResponse.setResponseDesc("Sms Send Success");
        }catch (Exception ex){
            logHelper.build()
                    .transactionID(requestRefid)
                    .logMsgType("Sms Sent")
                    .logStatus("Finished")
                    .logMsg("Failed")
                    .logDetailedMsg("Sms Failed to sent with error: "+ex.getMessage())
                    .info();
            status=HttpStatus.INTERNAL_SERVER_ERROR;
            apiResponse.setResponseCode("500");
            apiResponse.setRequestRefID(requestRefid);
            apiResponse.setResponseDesc("Sms Sending Failed");
        }

        return  Mono.just(ResponseEntity.status(status)
                .body(apiResponse));
    }

    /**
     * send email function
     * @param recipient
     * @param subject
     * @param text
     */
    public void senMail(String recipient,String subject,String text){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(configProperties.getEmailSender());
        message.setTo(recipient);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);

    }

    /**
     * send sms function
     * @param recipient
     * @param body
     */
    public void sendSms(String recipient,String body){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                        new PhoneNumber(recipient),
                        new PhoneNumber("883733"),
                        body)
                .create();
    }
}
