package com.ims.msloanrepaymentservice.scheduled;

import com.ims.msloanrepaymentservice.configs.ConfigProperties;
import com.ims.msloanrepaymentservice.entity.Loans;
import com.ims.msloanrepaymentservice.model.RepayLoanDetails;
import com.ims.msloanrepaymentservice.repository.LoanRepository;
import com.ims.msloanrepaymentservice.service.RepaymentService;
import com.ims.msloanrepaymentservice.utils.LogHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.List;

@Configuration
@EnableAsync
@EnableScheduling
@Slf4j
public class RepayLoanSchedule {

    @Autowired
    RepaymentService repaymentService;

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ConfigProperties configProperties;

    final LogHelper logHelper = LogHelper.withInitializer(log, (builder) ->
            builder
                    .operationName("Loan Repayment Cron Service")
                    .targetSystem("User"));

    /**
     * Cron job running every midnight to try and repay due loans
     */
    @Async
    @Scheduled(cron = "0 0 0 * * *")
    public void repayDueLoans() {
        LocalDate localDate = LocalDate.now();

        logHelper.build()
                .transactionID("")
                .logMsgType("Loan Repayment Cron job")
                .logStatus("Processing")
                .logMsg("Loan Repayment Cron job running....")
                .logDetailedMsg("Repaying loan  Cron running on : " + localDate)
                .info();

        List<Loans>  dueLoans = loanRepository.findByDueDate(localDate);
        RepayLoanDetails repayLoanDetails = null;
        /**
         * loops through due loans and try repay them
         */
        for (Loans dueLoan : dueLoans) {
            assert false;
            repayLoanDetails.setId(dueLoan.getId().toString());
            repayLoanDetails.setMsisdn(dueLoan.getMsisdn());
            repayLoanDetails.setRequestRefID(repayLoanDetails.getRequestRefID());
            repayLoanDetails.setSourceSystem(configProperties.getAppName());
            repayLoanDetails.setAmount(dueLoan.getAmount());

            repaymentService.repayLoan(repayLoanDetails);
        }
    }


}
