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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
     * Receives the request for processing
     * Does the repay loan business logic
     * @param repayLoanDetails
     */
    public void repayLoan(RepayLoanDetails repayLoanDetails) {
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

            loans.ifPresentOrElse(loan -> {

                        /**
                         * updates a record if the amount is not 5000
                         * any other value weather higher or less than 5000 as not as it is not 500
                         * updates loans table, computes the balance of the loan and update the status
                         */
                        BigDecimal amount = loan.getAmount().subtract(repayLoanDetails.getAmount());
                        String status="";
                        if(Integer.parseInt(String.valueOf(amount)) > 0){
                            status = "Partially Paid";
                        } else if (Integer.parseInt(String.valueOf(amount)) == 0) {
                            status = "Fully paid";
                        }else{
                            status ="Over paid";
                        }
                        loan.setAmount(amount);
                        loan.setStatus(status);
                        loan.setReferenceID(repayLoanDetails.getRequestRefID());
                        loan.setMsisdn(repayLoanDetails.getMsisdn());

                        loanRepository.save(loan);

                        NotificationDetails notificationDetails = new NotificationDetails();
                        notificationDetails.setRecipient(repayLoanDetails.getMsisdn());
                        notificationDetails.setSubject("Loan Repayment");
                        notificationDetails.setRequestRefID(repayLoanDetails.getRequestRefID());
                        notificationDetails.setSourceSystem(configProperties.getAppName());
                        notificationDetails.setText("Dear Customer Your loan of  " + loan.getAmount() + " Has been "+status);

                        rabbitMQSender.sendNotification(notificationDetails);

                        logHelper.build()
                                .transactionID(requestRefid)
                                .logMsgType("Credit Wallet")
                                .logStatus("Finished")
                                .logMsg("Success")
                                .logDetailedMsg("Loan Credit  Succeeded")
                                .info();

                    },
                    () -> {
                        /**
                         * Send notification to the user indicating the failure
                         * build the pojo and submit to a queue
                         */

                        NotificationDetails notificationDetails = new NotificationDetails();
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
                    });
        } catch (Exception ex) {

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
