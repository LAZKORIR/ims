package com.ims.apigateway.service;

import com.ims.apigateway.dto.ApiRequest;
import com.ims.apigateway.dto.ApiResponse;
import com.ims.apigateway.handler.RabbitMQSender;
import com.ims.apigateway.model.AddUserDetails;
import com.ims.apigateway.model.LoanDetails;
import com.ims.apigateway.utils.LogHelper;
import com.ims.apigateway.utils.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.math.BigDecimal;

@Service
@Slf4j
public class ApiGatewayService {

    @Autowired
    RabbitMQSender rabbitMQSender;

    final LogHelper logHelper = LogHelper.withInitializer(log, (builder) ->
            builder
                    .operationName("Api Gateway Service")
                    .targetSystem("Queue"));

    public Mono<ResponseEntity<ApiResponse>> addUser(@Valid @RequestBody ApiRequest apiRequest) {
        ApiResponse apiResponse = new ApiResponse();
        String requestRefid = apiRequest.getRequestRefID();
        apiResponse.setRequestRefID(requestRefid);
        HttpStatus status;

        logHelper.build()
                .transactionID(requestRefid)
                .logMsgType("Add User")
                .logStatus("Processing")
                .logMsg("Add User :  " + apiRequest.getFirstname())
                .logDetailedMsg("Adding user :  " + apiRequest.getFirstname() + " msisdn : " + Utility.maskMsisdn(apiRequest.getMsisdn()))
                .info();

        try {

            AddUserDetails addUserDetails = new AddUserDetails();
            addUserDetails.setEmail(apiRequest.getEmail());
            addUserDetails.setFirstname(apiRequest.getFirstname());
            addUserDetails.setLastname(apiRequest.getLastname());
            addUserDetails.setSourceSystem(apiRequest.getSourceSystem());
            addUserDetails.setMsisdn(apiRequest.getMsisdn());
            addUserDetails.setRequestRefID(requestRefid);

            logHelper.build()
                    .transactionID(requestRefid)
                    .logMsgType("Add User")
                    .logStatus("Finished")
                    .logMsg("Success")
                    .logDetailedMsg("Submitted to add addUserDetails queue  successfully")
                    .info();

            status = HttpStatus.OK;
            apiResponse.setResponseCode("200");
            apiResponse.setResponseDesc("Processing and will send notification to addUserDetails");

            rabbitMQSender.sendAddUser(addUserDetails);

        } catch (Exception ex) {
            logHelper.build()
                    .transactionID(requestRefid)
                    .logMsgType("Add User")
                    .logStatus("Finished")
                    .logMsg("Failed")
                    .logDetailedMsg("Sending to  add user queue Failed with error: " + ex.getMessage())
                    .info();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            apiResponse.setResponseCode("500");
            apiResponse.setRequestRefID(requestRefid);
            apiResponse.setResponseDesc("Adding user Failed");
        }
        return Mono.just(ResponseEntity.status(status)
                .body(apiResponse));
    }


    public Mono<ResponseEntity<ApiResponse>> requestLoan(ApiRequest apiRequest){
        ApiResponse apiResponse = new ApiResponse();
        String requestRefid = apiRequest.getRequestRefID();
        apiResponse.setRequestRefID(requestRefid);
        HttpStatus status = null;

        logHelper.build()
                .transactionID(requestRefid)
                .logMsgType("Request Loan")
                .logStatus("Processing")
                .logMsg("Request Loan of : "+apiRequest.getAmount())
                .logDetailedMsg("Request Loan for msisdn : " + Utility.maskMsisdn(apiRequest.getMsisdn()))
                .info();

        try {
            LoanDetails loanDetails = new LoanDetails();
            loanDetails.setAmount(BigDecimal.valueOf(Long.parseLong(apiRequest.getAmount())));
            loanDetails.setRequestRefID(apiRequest.getRequestRefID());
            loanDetails.setSourceSystem(apiRequest.getSourceSystem());
            loanDetails.setMsisdn(apiRequest.getMsisdn());
                logHelper.build()
                        .transactionID(requestRefid)
                        .logMsgType("Request Loan")
                        .logStatus("Finished")
                        .logMsg("processing...")
                        .logDetailedMsg("Loan request submitted for processing")
                        .info();

                status = HttpStatus.OK;
                apiResponse.setResponseCode("200");
                apiResponse.setResponseDesc("Loan request received and is being processed. user will receive a notification");

                rabbitMQSender.sendRequestLoan(loanDetails);

        }catch (Exception ex){
            ex.printStackTrace();
            logHelper.build()
                    .transactionID(requestRefid)
                    .logMsgType("Request Loan")
                    .logStatus("Finished")
                    .logMsg("Failed")
                    .logDetailedMsg("Request Loan Failed with error: " + ex.getMessage())
                    .info();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            apiResponse.setResponseCode("500");
            apiResponse.setRequestRefID(requestRefid);
            apiResponse.setResponseDesc("Request Loan Failed. Try again later");
        }
        return Mono.just(ResponseEntity.status(status)
                .body(apiResponse));
    }

    /**
     * Method that handles loan repayment requests
     * @param apiRequest
     * @return
     */
    public Mono<ResponseEntity<ApiResponse>> repayLoan(ApiRequest apiRequest){
        ApiResponse apiResponse = new ApiResponse();
        String requestRefid = apiRequest.getRequestRefID();
        apiResponse.setRequestRefID(requestRefid);
        HttpStatus status = null;

        logHelper.build()
                .transactionID(requestRefid)
                .logMsgType("Repay Loan")
                .logStatus("Processing")
                .logMsg("Repay Loan of : "+apiRequest.getAmount())
                .logDetailedMsg("Request Loan for msisdn : " + Utility.maskMsisdn(apiRequest.getMsisdn()))
                .info();

        try {
            LoanDetails loanDetails = new LoanDetails();
            loanDetails.setAmount(BigDecimal.valueOf(Long.parseLong(apiRequest.getAmount())));
            loanDetails.setRequestRefID(apiRequest.getRequestRefID());
            loanDetails.setSourceSystem(apiRequest.getSourceSystem());
            loanDetails.setMsisdn(apiRequest.getMsisdn());
            logHelper.build()
                    .transactionID(requestRefid)
                    .logMsgType("Request Loan")
                    .logStatus("Finished")
                    .logMsg("processing...")
                    .logDetailedMsg("Loan request submitted for processing")
                    .info();

            status = HttpStatus.OK;
            apiResponse.setResponseCode("200");
            apiResponse.setResponseDesc("Loan request received and is being processed. user will receive a notification");

            rabbitMQSender.sendRequestLoan(loanDetails);

        }catch (Exception ex){
            ex.printStackTrace();
            logHelper.build()
                    .transactionID(requestRefid)
                    .logMsgType("Request Loan")
                    .logStatus("Finished")
                    .logMsg("Failed")
                    .logDetailedMsg("Request Loan Failed with error: " + ex.getMessage())
                    .info();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            apiResponse.setResponseCode("500");
            apiResponse.setRequestRefID(requestRefid);
            apiResponse.setResponseDesc("Request Loan Failed. Try again later");
        }
        return Mono.just(ResponseEntity.status(status)
                .body(apiResponse));
    }
}
