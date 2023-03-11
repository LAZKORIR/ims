package com.ims.msnotificationservice.listener;

import com.ims.msnotificationservice.dto.ApiResponse;
import com.ims.msnotificationservice.model.NotificationDetails;
import com.ims.msnotificationservice.service.NotificationService;
import com.ims.msnotificationservice.utils.LogHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;


@Slf4j
public class NotificationListener {

    @Autowired
    NotificationService notificationService;

    final LogHelper logHelper = LogHelper.withInitializer(log, (builder) ->
            builder
                    .operationName("Notification Service")
                    .targetSystem("Smtp serves"));

    /**
     * Notification listener
     * expects NotificationDetails Params to send notification
     * @param notificationDetails
     */
    @RabbitListener(queues = "notification.queue")
    public void sendNotification(NotificationDetails notificationDetails){

        String mode = notificationDetails.getMode();

        if(mode.equalsIgnoreCase("email")){
            sendMail(notificationDetails);
        } else if (mode.equalsIgnoreCase("sms")) {
            sendSms(notificationDetails);
        }else{
            sendMail(notificationDetails);
            sendSms(notificationDetails);
        }


    }

    /**
     * sending sms function
     * sends sms to recipient and body provided
     * @param notificationDetails
     */
    public void sendSms(NotificationDetails notificationDetails){

        String recipient = notificationDetails.getRecipient();
        String refId = notificationDetails.getRequestRefID();
        String text = notificationDetails.getText();
        logHelper.build()
                .transactionID(refId)
                .logMsgType("Send SMS")
                .logStatus("Processing")
                .logMsg("Sending  sms :  ")
                .logDetailedMsg("Sending sms to :  "+ recipient +" with body : "+text)
                .info();
        try {

        notificationService.sendSms(recipient, text);
            logHelper.build()
                    .transactionID(refId)
                    .logMsgType("SMS Sent")
                    .logStatus("Finished")
                    .logMsg("Sending  sms :  " )
                    .logDetailedMsg("SMS sent to :  " + recipient + " with body : " + text)
                    .info();

        }catch (Exception ex){
            logHelper.build()
                    .transactionID(refId)
                    .logMsgType("SMS Sent")
                    .logStatus("Finished")
                    .logMsg("Failed")
                    .logDetailedMsg("SMS Sending Failed with error: "+ex.getMessage())
                    .info();
        }
    }

    /**
     * Send email function
     * sends email to recipient provided with subject and text
     * @param notificationDetails
     */
    public void sendMail(NotificationDetails notificationDetails){
        String recipient = notificationDetails.getRecipient();
        String refId = notificationDetails.getRequestRefID();
        String subject = notificationDetails.getSubject();
        String text = notificationDetails.getText();

        logHelper.build()
                .transactionID(refId)
                .logMsgType("Send Email")
                .logStatus("Processing")
                .logMsg("Email subject :  "+subject)
                .logDetailedMsg("Sending email to :  "+ recipient +" with text : "+text)
                .info();

        try {

            notificationService.senMail(recipient,subject,text);

            logHelper.build()
                    .transactionID(refId)
                    .logMsgType("Email Sent")
                    .logStatus("Finished")
                    .logMsg("Email subject :  " + subject)
                    .logDetailedMsg("Email sent to :  " + recipient + " with text : " + text)
                    .info();

        }catch (Exception ex){
            logHelper.build()
                    .transactionID(refId)
                    .logMsgType("Email Sent")
                    .logStatus("Finished")
                    .logMsg("Failed")
                    .logDetailedMsg("Email Failed to sent with error: "+ex.getMessage())
                    .info();
        }
    }
}
