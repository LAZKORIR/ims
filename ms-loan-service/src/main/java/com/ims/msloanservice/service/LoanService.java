package com.ims.msloanservice.service;

import com.ims.msloanservice.entity.Products;
import com.ims.msloanservice.model.ApiRequest;
import com.ims.msloanservice.model.ApiResponse;
import com.ims.msloanservice.repository.ProductRepository;
import com.ims.msloanservice.utils.LogHelper;
import com.ims.msloanservice.utils.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;


@Service
@Slf4j
public class LoanService {
    @Autowired
    ProductRepository productRepository;

    final LogHelper logHelper = LogHelper.withInitializer(log, (builder) ->
            builder
                    .operationName("Loan Service")
                    .targetSystem("User service"));


    public Mono<ResponseEntity<ApiResponse>> requestLoan(ApiRequest apiRequest){
        ApiResponse apiResponse = new ApiResponse();
        String requestRefid = apiRequest.getRequestRefID();
        apiResponse.setRequestRefID(requestRefid);
        HttpStatus status = null;

        logHelper.build()
                .transactionID(requestRefid)
                .logMsgType("Query Products")
                .logStatus("Processing")
                .logMsg("Querying....")
                .logDetailedMsg("Query Products for msisdn : " + Utility.maskMsisdn(apiRequest.getMsisdn()))
                .info();

        try {
            List<Products> availableProducts = productRepository.findAll();
            if(availableProducts.isEmpty()){

                logHelper.build()
                        .transactionID(requestRefid)
                        .logMsgType("Query Products")
                        .logStatus("Finished")
                        .logMsg("Failed")
                        .logDetailedMsg("Querying available products  Failed")
                        .info();
                apiResponse.setResponseCode("404");
                apiResponse.setResponseDesc("No Active Product was Found");
                apiResponse.setTransactionID("");
                apiResponse.setRequestRefID(requestRefid);

                status = HttpStatus.NOT_FOUND;

            }else{

                logHelper.build()
                        .transactionID(requestRefid)
                        .logMsgType("Query Products")
                        .logStatus("Finished")
                        .logMsg("Success")
                        .logDetailedMsg("Querying available products  Succeeded")
                        .info();

                status = HttpStatus.OK;
                apiResponse.setResponseCode("200");
                apiResponse.setResponseDesc("Loan products");
                apiResponse.setBody(availableProducts);

            }
        }catch (Exception ex){
            ex.printStackTrace();
            logHelper.build()
                    .transactionID(requestRefid)
                    .logMsgType("Query Products")
                    .logStatus("Finished")
                    .logMsg("Failed")
                    .logDetailedMsg("Querying products Failed with error: " + ex.getMessage())
                    .info();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            apiResponse.setResponseCode("500");
            apiResponse.setRequestRefID(requestRefid);
            apiResponse.setResponseDesc("Querying products Failed");
        }
        return Mono.just(ResponseEntity.status(status)
                .body(apiResponse));
    }
}
