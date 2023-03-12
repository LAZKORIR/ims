package com.ims.msloanrepaymentservice.listener;

import com.ims.msloanrepaymentservice.model.RepayLoanDetails;
import com.ims.msloanrepaymentservice.service.RepaymentService;
import com.ims.msloanrepaymentservice.utils.LogHelper;
import com.ims.msloanrepaymentservice.utils.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class RepayLoanListener {

    @Autowired
    RepaymentService repaymentService;

    final LogHelper logHelper = LogHelper.withInitializer(log, (builder) ->
            builder
                    .operationName("Loan Repayment Service")
                    .targetSystem("User"));

    /**
     * Listener for manual repayment of loans
     * @param repayLoanDetails
     */
    @RabbitListener(queues = "loanRepay.queue")
    public void repayLoan(RepayLoanDetails repayLoanDetails){

        logHelper.build()
                .transactionID("")
                .logMsgType("Manual Loan Repayment")
                .logStatus("Processing")
                .logMsg("Manual Loan Repayment ....")
                .logDetailedMsg("Repaying loan manually for  : " + Utility.maskMsisdn(repayLoanDetails.getMsisdn()))
                .info();

        repaymentService.repayLoan(repayLoanDetails);

    }
}
