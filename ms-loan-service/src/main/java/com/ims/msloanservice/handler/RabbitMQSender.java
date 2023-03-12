package com.ims.msloanservice.handler;

import com.ims.msloanservice.config.ConfigProperties;
import com.ims.msloanservice.model.NotificationDetails;
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
        rabbitTemplate.convertAndSend(configProperties.getExchange(), configProperties.getNotificationQueue(), notificationDetails);
        System.out.println("Send loan Details   msg = " + notificationDetails);

    }
}
