package com.ims.apigateway.service;

import com.ims.apigateway.configs.ConfigProperties;
import com.ims.apigateway.dto.ApiRequest;
import com.ims.apigateway.dto.ApiResponse;
import com.ims.apigateway.handler.RabbitMQSender;
import com.ims.apigateway.model.AddUserDetails;
import com.ims.apigateway.model.LoanDetails;
import com.ims.apigateway.model.RepayLoanDetails;
import com.ims.apigateway.utils.LogHelper;
import com.ims.apigateway.utils.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.math.BigDecimal;

@Service
@Slf4j
public class ApiGatewayService {

    @Autowired
    RabbitMQSender rabbitMQSender;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ConfigProperties configProperties;


    final LogHelper logHelper = LogHelper.withInitializer(log, (builder) ->
            builder
                    .operationName("Api Gateway Service")
                    .targetSystem("Queue"));

    /**
     * This function is used to register users to the system
     * @param apiRequest
     * @return
     */
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


    /**
     * This function picks request loan request and sends to queue for processing
     * @param apiRequest
     * @return
     */
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
            RepayLoanDetails repayLoanDetails = new RepayLoanDetails();
            repayLoanDetails.setAmount(BigDecimal.valueOf(Long.parseLong(apiRequest.getAmount())));
            repayLoanDetails.setRequestRefID(apiRequest.getRequestRefID());
            repayLoanDetails.setSourceSystem(apiRequest.getSourceSystem());
            repayLoanDetails.setMsisdn(apiRequest.getMsisdn());
            repayLoanDetails.setId(apiRequest.getId());

            logHelper.build()
                    .transactionID(requestRefid)
                    .logMsgType("Repay Loan")
                    .logStatus("Finished")
                    .logMsg("processing...")
                    .logDetailedMsg("Repay Loan request submitted for processing")
                    .info();

            status = HttpStatus.OK;
            apiResponse.setResponseCode("200");
            apiResponse.setResponseDesc("Repay Loan request received and is being processed. user will receive a notification");

            rabbitMQSender.sendRepayLoan(repayLoanDetails);

        }catch (Exception ex){
            ex.printStackTrace();
            logHelper.build()
                    .transactionID(requestRefid)
                    .logMsgType("Repay Loan")
                    .logStatus("Finished")
                    .logMsg("Failed")
                    .logDetailedMsg("Repay Loan Failed with error: " + ex.getMessage())
                    .info();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            apiResponse.setResponseCode("500");
            apiResponse.setRequestRefID(requestRefid);
            apiResponse.setResponseDesc("Repay Loan Failed. Try again later");
        }
        return Mono.just(ResponseEntity.status(status)
                .body(apiResponse));
    }

    /**
     * Check limit api
     * gets data immediately since the user needs to select loan product
     * @param apiRequest
     * @return
     */
    public Mono<ResponseEntity<ApiResponse>> checkLimit(ApiRequest apiRequest){

        logHelper.build()
                .transactionID(apiRequest.getRequestRefID())
                .logMsgType("Check Loan Limit")
                .logStatus("Processing")
                .logMsg("Check Loan Limit : ")
                .logDetailedMsg("Check Loan Limit for msisdn : " + Utility.maskMsisdn(apiRequest.getMsisdn()))
                .info();

        ApiResponse apiResponse=
                restTemplate.postForEntity(configProperties.getCheckLimitEndpoint()+"checkLimit",apiRequest,ApiResponse.class).getBody();
        
        return Mono.just(ResponseEntity.status(HttpStatus.OK).body(apiResponse));
    }

    /**
     * This function returns user details using supplied msisdn
     * @param apiRequest
     * @return
     */
    public Mono<ResponseEntity<ApiResponse>> getUser(ApiRequest apiRequest){

        logHelper.build()
                .transactionID(apiRequest.getRequestRefID())
                .logMsgType("Get User")
                .logStatus("Processing")
                .logMsg("Get Single user : ")
                .logDetailedMsg("Get Single user registered under msisdn : " + Utility.maskMsisdn(apiRequest.getMsisdn()))
                .info();

        ApiResponse apiResponse=
                restTemplate.postForEntity(configProperties.getCheckLimitEndpoint()+"addUser",apiRequest,ApiResponse.class).getBody();

        return Mono.just(ResponseEntity.status(HttpStatus.OK).body(apiResponse));
    }

    /**
     * this function is used for returning all registered users
     * @return
     */
    public Mono<ResponseEntity<ApiResponse>> getAllUsers(){

        logHelper.build()
                .transactionID("")
                .logMsgType("Get All Users")
                .logStatus("Processing")
                .logMsg("Get All Users : ")
                .logDetailedMsg("Get All Users registered : ")
                .info();

        ApiResponse apiResponse=
                restTemplate.getForObject(configProperties.getCheckLimitEndpoint()+"getAllUsers",ApiResponse.class);

        return Mono.just(ResponseEntity.status(HttpStatus.OK).body(apiResponse));
    }
}
