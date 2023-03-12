package com.ims.apigateway;

import com.ims.apigateway.controller.ApiGatewayController;
import com.ims.apigateway.dto.ApiRequest;
import com.ims.apigateway.dto.ApiResponse;
import com.ims.apigateway.model.AddUserDetails;
import com.ims.apigateway.service.ApiGatewayService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(ApiGatewayController.class)
public class ApiGatewayApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private ApiGatewayService apiGatewayService;

	@Test
	public void addUser() {

		ApiRequest apiRequest = new ApiRequest();
		apiRequest.setRequestRefID(UUID.randomUUID().toString());
		apiRequest.setAmount("200");
		apiRequest.setFirstname("Lazz");
		apiRequest.setLastname("Korir");
		apiRequest.setMsisdn("254723846453");
		apiRequest.setSourceSystem("Testing app");
		apiRequest.setEmail("lazaruskorir@gmail.com");

		when(apiGatewayService.addUser(apiRequest)).thenReturn(Mono.just(ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("dhshdhsd","200","success","",""))));

		webTestClient
				.post().uri("/api/v1/addUser")
				.body(Mono.just(apiRequest),ApiRequest.class)
				.exchange()
				.expectStatus()
				.isOk();//200
	}

	@Test
	public void checkLimit() {

		ApiRequest apiRequest = new ApiRequest();
		apiRequest.setRequestRefID(UUID.randomUUID().toString());
		apiRequest.setAmount("200");
		apiRequest.setMsisdn("254723846453");
		apiRequest.setSourceSystem("Testing app");

		when(apiGatewayService.checkLimit(apiRequest)).thenReturn(Mono.just(ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("dhshdhsd","200","success","",""))));

		webTestClient
				.post().uri("/api/v1/check-limit")
				.body(Mono.just(apiRequest),ApiRequest.class)
				.exchange()
				.expectStatus()
				.isOk();//200
	}

	@Test
	public void getUser() {

		ApiRequest apiRequest = new ApiRequest();
		apiRequest.setRequestRefID(UUID.randomUUID().toString());
		apiRequest.setAmount("200");
		apiRequest.setMsisdn("254723846423");
		apiRequest.setSourceSystem("Testing app");

		when(apiGatewayService.getUser(apiRequest)).thenReturn(Mono.just(ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("dhshdhsd","200","Success","",""))));

		webTestClient
				.post().uri("/api/v1/getUser")
				.body(Mono.just(apiRequest),ApiRequest.class)
				.exchange()
				.expectStatus()
				.isOk();//200
	}

	@Test
	public void getAllUsers() {

		when(apiGatewayService.getAllUsers()).thenReturn(Mono.just(ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("dhshdhsd","200","Success","",""))));

		webTestClient
				.get().uri("/api/v1/getAllUsers")
				.exchange()
				.expectStatus()
				.isOk();//200

	}

	@Test
	public void requestLoan() {

		ApiRequest apiRequest = new ApiRequest();
		apiRequest.setRequestRefID(UUID.randomUUID().toString());
		apiRequest.setAmount("200");
		apiRequest.setMsisdn("254723846453");
		apiRequest.setSourceSystem("Testing app");
		apiRequest.setProductId("10001");
		apiRequest.setUserId(101);

		when(apiGatewayService.requestLoan(apiRequest)).thenReturn(Mono.just(ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("dhshdhsd","200","success","",""))));

		webTestClient
				.post().uri("/api/v1/request-loan")
				.body(Mono.just(apiRequest),ApiRequest.class)
				.exchange()
				.expectStatus()
				.isOk();//200
	}

	@Test
	public void repayLoan() {

		ApiRequest apiRequest = new ApiRequest();
		apiRequest.setRequestRefID(UUID.randomUUID().toString());
		apiRequest.setAmount("200");
		apiRequest.setMsisdn("254723846453");
		apiRequest.setSourceSystem("Testing app");
		apiRequest.setUserId(101);

		when(apiGatewayService.repayLoan(apiRequest)).thenReturn(Mono.just(ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("dhshdhsd","200","success","",""))));

		webTestClient
				.post().uri("/api/v1/loan-repayment")
				.body(Mono.just(apiRequest),ApiRequest.class)
				.exchange()
				.expectStatus()
				.isOk();//200
	}
}

