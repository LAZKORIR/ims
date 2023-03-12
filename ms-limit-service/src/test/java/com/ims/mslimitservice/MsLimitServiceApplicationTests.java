package com.ims.mslimitservice;

import com.ims.mslimitservice.controller.LimitController;
import com.ims.mslimitservice.model.ApiRequest;
import com.ims.mslimitservice.model.ApiResponse;
import com.ims.mslimitservice.service.LimitService;
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
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(LimitController.class)
public class MsLimitServiceApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private LimitService limitService;

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

		when(limitService.addUser(apiRequest)).thenReturn(Mono.just(ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("dhshdhsd","200","success","",""))));

		webTestClient
				.post().uri("/user/addUser")
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

		when(limitService.checkLimit(apiRequest)).thenReturn(Mono.just(ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("dhshdhsd","200","success","",""))));

		webTestClient
				.post().uri("/user/check-limit")
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

		when(limitService.getUser(apiRequest)).thenReturn(Mono.just(ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("dhshdhsd","200","Success","",""))));

		webTestClient
				.post().uri("/user/getUser")
				.body(Mono.just(apiRequest),ApiRequest.class)
				.exchange()
				.expectStatus()
				.isOk();//200
	}

	@Test
	public void getAllUsers() {

		when(limitService.getAllUsers()).thenReturn(Mono.just(ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("dhshdhsd","200","Success","",""))));

		webTestClient
				.get().uri("/user/getAllUsers")
				.exchange()
				.expectStatus()
				.isOk();//200

	}
}
