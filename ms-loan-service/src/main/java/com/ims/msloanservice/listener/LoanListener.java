package com.ims.msloanservice.listener;


import com.ims.msloanservice.model.LoanDetails;
import com.ims.msloanservice.service.LoanService;
import com.ims.msloanservice.utils.LogHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class LoanListener {

    @Autowired
    LoanService loanService;

    final LogHelper logHelper = LogHelper.withInitializer(log, (builder) ->
            builder
                    .operationName("Loan Service")
                    .targetSystem("User"));

    /**
     * Listener for loan requests
     * @param loanDetails
     */
    @RabbitListener(queues = "loan.queue")
    public void requestLoan(LoanDetails loanDetails){

        loanService.requestLoan(loanDetails);

    }
}
