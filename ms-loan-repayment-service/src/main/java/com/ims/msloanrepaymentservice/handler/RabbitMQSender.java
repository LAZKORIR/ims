package com.ims.msloanrepaymentservice.handler;


import com.ims.msloanrepaymentservice.configs.ConfigProperties;
import com.ims.msloanrepaymentservice.model.NotificationDetails;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {

    @Autowired
    ConfigProperties configProperties;

    @Autowired
    private AmqpTemplate rabbitTemplate;


    public void sendNotification(NotificationDetails notificationDetails) {
        rabbitTemplate.convertAndSend(configProperties.getExchange(), configProperties.getNotificationRoutingKey(), notificationDetails);
        System.out.println("Send loan repayment Details   msg = " + notificationDetails);

    }
}
