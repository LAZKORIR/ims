package com.ims.apigateway.handler;

import com.ims.apigateway.configs.ConfigProperties;
import com.ims.apigateway.model.AddUserDetails;
import com.ims.apigateway.model.LoanDetails;
import com.ims.apigateway.model.RepayLoanDetails;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {

    @Autowired
    ConfigProperties configProperties;

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void sendAddUser(AddUserDetails addUserDetails) {
        rabbitTemplate.convertAndSend(configProperties.getExchange(), configProperties.getLimitRoutingKey(), addUserDetails);
        System.out.println("Send add   msg = " + addUserDetails);

    }

    public void sendRequestLoan(LoanDetails loanDetails) {
        rabbitTemplate.convertAndSend(configProperties.getExchange(), configProperties.getLoanRoutingKey(), loanDetails);
        System.out.println("Send loan Details   msg = " + loanDetails);

    }

    public void sendRepayLoan(RepayLoanDetails repayLoanDetails) {
        rabbitTemplate.convertAndSend(configProperties.getExchange(), configProperties.getRepayLoanRoutingKey(), repayLoanDetails);
        System.out.println("Send loan repayment Details   msg = " + repayLoanDetails);

    }
}
