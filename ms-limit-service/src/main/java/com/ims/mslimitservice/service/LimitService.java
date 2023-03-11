package com.ims.mslimitservice.service;

import com.ims.mslimitservice.UsersRepository;
import com.ims.mslimitservice.entity.UserDetails;
import com.ims.mslimitservice.model.ApiRequest;
import com.ims.mslimitservice.model.ApiResponse;
import com.ims.mslimitservice.utils.LogHelper;
import com.ims.mslimitservice.utils.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class LimitService {

    @Autowired
    UsersRepository usersRepository;

    final LogHelper logHelper = LogHelper.withInitializer(log, (builder) ->
            builder
                    .operationName("User Service")
                    .targetSystem("UserDetails"));

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

        UserDetails checkAvailability = usersRepository.findByMsisdn(apiRequest.getMsisdn());
        if(checkAvailability != null){
            apiResponse.setResponseCode("409");
            apiResponse.setResponseDesc("User already registered");
            apiResponse.setTransactionID("");
            apiResponse.setRequestRefID(requestRefid);

            status = HttpStatus.CONFLICT;

        }else {
            try {

                UserDetails user = new UserDetails();
                user.setEmail(apiRequest.getEmail());
                user.setFirstname(apiRequest.getFirstname());
                user.setLastname(apiRequest.getLastname());
                user.setSourceSystem(apiRequest.getSourceSystem());
                user.setMsisdn(apiRequest.getMsisdn());
                user.setRequestRefID(requestRefid);

                usersRepository.save(user);
                logHelper.build()
                        .transactionID(requestRefid)
                        .logMsgType("Add User")
                        .logStatus("Finished")
                        .logMsg("Success")
                        .logDetailedMsg("Adding user  Succeeded")
                        .info();

                status = HttpStatus.OK;
                apiResponse.setResponseCode("200");
                apiResponse.setResponseDesc("Adding user  Succeeded");

            } catch (Exception ex) {
                logHelper.build()
                        .transactionID(requestRefid)
                        .logMsgType("Add User")
                        .logStatus("Finished")
                        .logMsg("Failed")
                        .logDetailedMsg("Adding user Failed with error: " + ex.getMessage())
                        .info();
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                apiResponse.setResponseCode("500");
                apiResponse.setRequestRefID(requestRefid);
                apiResponse.setResponseDesc("Adding user Failed");
            }
        }
        return Mono.just(ResponseEntity.status(status)
                .body(apiResponse));
    }

    public Mono<ResponseEntity<ApiResponse>> getAllUsers() {
        ApiResponse apiResponse = new ApiResponse();
        String requestRefid = String.valueOf(UUID.randomUUID());
        apiResponse.setRequestRefID(requestRefid);
        HttpStatus status = null;

        logHelper.build()
                .transactionID(requestRefid)
                .logMsgType("Get all UserDetails")
                .logStatus("Processing")
                .logMsg("Getting all  UserDetails  ")
                .logDetailedMsg("Retrieving users")
                .info();
        try {

        List<UserDetails> users = usersRepository.findAll();

            logHelper.build()
                    .transactionID(requestRefid)
                    .logMsgType("Get all UserDetails")
                    .logStatus("Finished")
                    .logMsg("Success")
                    .logDetailedMsg("Querying all users  Succeeded")
                    .info();

            status = HttpStatus.OK;
            apiResponse.setResponseCode("200");
            apiResponse.setResponseDesc("Getting all user  Succeeded");
            apiResponse.setBody(users);


        }catch (Exception ex) {
            logHelper.build()
                    .transactionID(requestRefid)
                    .logMsgType("Get  all UserDetails")
                    .logStatus("Finished")
                    .logMsg("Failed")
                    .logDetailedMsg("querying all users Failed with error: " + ex.getMessage())
                    .info();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            apiResponse.setResponseCode("500");
            apiResponse.setRequestRefID(requestRefid);
            apiResponse.setResponseDesc("querying all users Failed");
        }
        return Mono.just(ResponseEntity.status(status)
                .body(apiResponse));
    }

    public Mono<ResponseEntity<ApiResponse>> getUser(@Valid @RequestBody ApiRequest apiRequest) {
        ApiResponse apiResponse = new ApiResponse();
        String requestRefid = apiRequest.getRequestRefID();
        apiResponse.setRequestRefID(requestRefid);
        HttpStatus status = null;

        logHelper.build()
                .transactionID(requestRefid)
                .logMsgType("Get User")
                .logStatus("Processing")
                .logMsg("Get User :  " + apiRequest.getFirstname())
                .logDetailedMsg("Retrieving user using msisdn : " + Utility.maskMsisdn(apiRequest.getMsisdn()))
                .info();
        try {

            UserDetails checkAvailability = usersRepository.findByMsisdn(apiRequest.getMsisdn());

            if(checkAvailability != null){
                logHelper.build()
                        .transactionID(requestRefid)
                        .logMsgType("Get User")
                        .logStatus("Finished")
                        .logMsg("Success")
                        .logDetailedMsg("Querying user  Succeeded")
                        .info();

                status = HttpStatus.OK;
                apiResponse.setResponseCode("200");
                apiResponse.setResponseDesc("Getting user  Succeeded");
                apiResponse.setBody(checkAvailability);


            }else {

                apiResponse.setResponseCode("404");
                apiResponse.setResponseDesc("User Not Found");
                apiResponse.setTransactionID("");
                apiResponse.setRequestRefID(requestRefid);

                status = HttpStatus.NOT_FOUND;

            }
        }catch (Exception ex) {
            logHelper.build()
                    .transactionID(requestRefid)
                    .logMsgType("Get User")
                    .logStatus("Finished")
                    .logMsg("Failed")
                    .logDetailedMsg("querying user Failed with error: " + ex.getMessage())
                    .info();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            apiResponse.setResponseCode("500");
            apiResponse.setRequestRefID(requestRefid);
            apiResponse.setResponseDesc("querying user Failed");
        }
        return Mono.just(ResponseEntity.status(status)
                .body(apiResponse));
    }
}
