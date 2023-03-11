package com.ims.msloanrepaymentservice.service;

import com.ims.msloanrepaymentservice.configs.ConfigProperties;
import com.ims.msloanrepaymentservice.dto.ApiResponse;
import com.ims.msloanrepaymentservice.entity.Loans;
import com.ims.msloanrepaymentservice.handler.RabbitMQSender;
import com.ims.msloanrepaymentservice.model.NotificationDetails;
import com.ims.msloanrepaymentservice.model.RepayLoanDetails;
import com.ims.msloanrepaymentservice.repository.LoanRepository;
import com.ims.msloanrepaymentservice.utils.LogHelper;
import com.ims.msloanrepaymentservice.utils.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RepaymentService {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ConfigProperties configProperties;

    @Autowired
    RabbitMQSender rabbitMQSender;

    final LogHelper logHelper = LogHelper.withInitializer(log, (builder) ->
            builder
                    .operationName("Loan Repayment Service")
                    .targetSystem("Notification Service"));

    /**
     * Rabbit mq lister for loan requests
     * Recieves the request for processing
     * @param repayLoanDetails
     */
    @RabbitListener(queues = "loan.queue")
    public void repayLoan(RepayLoanDetails repayLoanDetails){
        ApiResponse apiResponse = new ApiResponse();
        String requestRefid = repayLoanDetails.getRequestRefID();
        apiResponse.setRequestRefID(requestRefid);

        logHelper.build()
                .transactionID(requestRefid)
                .logMsgType("Loan Repayment")
                .logStatus("Processing")
                .logMsg("Loan Repayment....")
                .logDetailedMsg("Repaying loan  for msisdn : " + Utility.maskMsisdn(repayLoanDetails.getMsisdn()))
                .info();

        try {

            Optional<Loans> loans = loanRepository.findById(Integer.valueOf(repayLoanDetails.getId()));

            loans.ifPresent(loan -> {

                /**
                 * updates a record if the amount is not 5000
                 */

                loan.setAmount(loan.getAmount().subtract(repayLoanDetails.getAmount()));
                loan.setStatus("Paid");
                loan.setReferenceID(repayLoanDetails.getRequestRefID());
                loan.setMsisdn(repayLoanDetails.getMsisdn());

                loanRepository.save(loan);

                NotificationDetails notificationDetails= new NotificationDetails();
                notificationDetails.setRecipient(repayLoanDetails.getMsisdn());
                notificationDetails.setSubject("Loan Repayment");
                notificationDetails.setRequestRefID(repayLoanDetails.getRequestRefID());
                notificationDetails.setSourceSystem(configProperties.getAppName());
                notificationDetails.setText("Dear Customer Your loan of  "+repayLoanDetails.getAmount() +" Has been Repaid");

                rabbitMQSender.sendNotification(notificationDetails);

                logHelper.build()
                        .transactionID(requestRefid)
                        .logMsgType("Credit Wallet")
                        .logStatus("Finished")
                        .logMsg("Success")
                        .logDetailedMsg("Loan Credit  Succeeded")
                        .info();

            });

                NotificationDetails notificationDetails= new NotificationDetails();
                notificationDetails.setRecipient(repayLoanDetails.getMsisdn());
                notificationDetails.setSubject("Loan Repayment Failed");
                notificationDetails.setRequestRefID(repayLoanDetails.getRequestRefID());
                notificationDetails.setSourceSystem(configProperties.getAppName());
                notificationDetails.setText("Dear Customer We are not able to complete your loan repayment request at the moment. Try again later");

                rabbitMQSender.sendNotification(notificationDetails);

                logHelper.build()
                        .transactionID(requestRefid)
                        .logMsgType("Loan Repayment")
                        .logStatus("Finished")
                        .logMsg("Failed")
                        .logDetailedMsg("Failed to Repay your loan")
                        .info();

        }catch (Exception ex){

            logHelper.build()
                    .transactionID(requestRefid)
                    .logMsgType("Loan Repayment")
                    .logStatus("Finished")
                    .logMsg("Failed")
                    .logDetailedMsg("Loan Repayment Failed with error: " + ex.getMessage())
                    .info();

        }
    }
}
