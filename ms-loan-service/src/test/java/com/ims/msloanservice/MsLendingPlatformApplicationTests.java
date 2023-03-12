package com.ims.msloanservice;

import com.ims.msloanservice.dto.ApiRequest;
import com.ims.msloanservice.dto.ApiResponse;
import com.ims.msloanservice.service.LoanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.mockito.Mockito.when;

@SpringBootTest
class MsLendingPlatformApplicationTests {

	@Autowired
	private WebTestClient webTestClient;
	@MockBean
	LoanService loanService;
	@Test
	public void requestLoan() {

		ApiRequest apiRequest = new ApiRequest();
		apiRequest.setRequestRefID(UUID.randomUUID().toString());
		apiRequest.setAmount("200");
		apiRequest.setMsisdn("254723846453");
		apiRequest.setSourceSystem("Testing app");
		apiRequest.setProductId("10001");
		apiRequest.setUserId(101);

		when(loanService.requestLoan(apiRequest)).thenReturn(Mono.just(ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("dhshdhsd","200","success","",""))));

		webTestClient
				.post().uri("/loan/request-loan")
				.body(Mono.just(apiRequest),ApiRequest.class)
				.exchange()
				.expectStatus()
				.isOk();//200
	}

}
