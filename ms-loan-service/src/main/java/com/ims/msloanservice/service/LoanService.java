package com.ims.msloanservice.service;

import com.ims.msloanservice.config.ConfigProperties;
import com.ims.msloanservice.dto.ApiResponse;
import com.ims.msloanservice.entity.Loans;
import com.ims.msloanservice.handler.RabbitMQSender;
import com.ims.msloanservice.model.LoanDetails;
import com.ims.msloanservice.model.NotificationDetails;
import com.ims.msloanservice.repository.LoanRepository;
import com.ims.msloanservice.repository.ProductRepository;
import com.ims.msloanservice.utils.LogHelper;
import com.ims.msloanservice.utils.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;


@Service
@Slf4j
public class LoanService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    RabbitMQSender rabbitMQSender;

    @Autowired
    ConfigProperties configProperties;

    final LogHelper logHelper = LogHelper.withInitializer(log, (builder) ->
            builder
                    .operationName("Loan Service")
                    .targetSystem("User service"));

    /**
     * Rabbit mq lister for loan requests
     * Recieves the request for processing
     * @param loanDetails
     */
    @RabbitListener(queues = "loan.queue")
    public void requestLoan(LoanDetails loanDetails){
        ApiResponse apiResponse = new ApiResponse();
        String requestRefid = loanDetails.getRequestRefID();
        apiResponse.setRequestRefID(requestRefid);

        logHelper.build()
                .transactionID(requestRefid)
                .logMsgType("Credit Wallet")
                .logStatus("Processing")
                .logMsg("Crediting Wallet....")
                .logDetailedMsg("Crediting Wallet for msisdn : " + Utility.maskMsisdn(loanDetails.getMsisdn()))
                .info();

        try {

            String  creditLoan = creditWallet(loanDetails.getAmount());
            if(creditLoan.equalsIgnoreCase("success")){
                /**
                 * create a record if the amount is not 5000
                 */
                LocalDate localDate;
                if(loanDetails.getProductID().equalsIgnoreCase("1001")){
                    localDate=LocalDate.now().plusDays( 15);
                }else{
                    localDate=LocalDate.now().plusDays( 30 );
                }
                Loans loans = new Loans();
                loans.setAmount(loanDetails.getAmount());
                loans.setDueDate(localDate);
                loans.setProductID(loanDetails.getProductID());
                loans.setStatus("Credited");
                loans.setReferenceID(loanDetails.getRequestRefID());
                loans.setMsisdn(loanDetails.getMsisdn());

                loanRepository.save(loans);

                NotificationDetails notificationDetails= new NotificationDetails();
                notificationDetails.setRecipient(loanDetails.getMsisdn());
                notificationDetails.setSubject("Loan Credited");
                notificationDetails.setRequestRefID(loanDetails.getRequestRefID());
                notificationDetails.setSourceSystem(configProperties.getAppName());
                notificationDetails.setText("Dear Customer Your loan of  "+loanDetails.getAmount() +" Has been credited to your wallet");

                rabbitMQSender.sendNotification(notificationDetails);

                logHelper.build()
                        .transactionID(requestRefid)
                        .logMsgType("Credit Wallet")
                        .logStatus("Finished")
                        .logMsg("Success")
                        .logDetailedMsg("Loan Credit  Succeeded")
                        .info();
            }else {
                NotificationDetails notificationDetails= new NotificationDetails();
                notificationDetails.setRecipient(loanDetails.getMsisdn());
                notificationDetails.setSubject("Loan Request Failed");
                notificationDetails.setRequestRefID(loanDetails.getRequestRefID());
                notificationDetails.setSourceSystem(configProperties.getAppName());
                notificationDetails.setText("Dear Customer We are not able to complete your request at the moment. Try again later");

                rabbitMQSender.sendNotification(notificationDetails);

                logHelper.build()
                        .transactionID(requestRefid)
                        .logMsgType("Credit Wallet")
                        .logStatus("Finished")
                        .logMsg("Failed")
                        .logDetailedMsg("Failed to credit your wallet")
                        .info();
            }



        }catch (Exception ex){

            logHelper.build()
                    .transactionID(requestRefid)
                    .logMsgType("Credit Wallet")
                    .logStatus("Finished")
                    .logMsg("Failed")
                    .logDetailedMsg("Credit Wallet Failed with error: " + ex.getMessage())
                    .info();

        }
    }

    /**
     * Returns failed status for an amount equal to 500
     * @param amount
     * @return
     */
    public String creditWallet(BigDecimal amount){
        String status = "";
        if(amount.equals( 5000)){
            status = "failed";
        }else{
            status="success";
        }
        return status;
    }

}
