package com.ims.apigateway;

import com.ims.apigateway.dto.ApiResponse;
import com.ims.apigateway.model.AddUserDetails;
import com.ims.apigateway.service.ApiGatewayService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@SpringBootTest
class ApiGatewayApplicationTests {
	@Autowired
	WebTestClient webTestClient;

	@Autowired
	ApiGatewayService apiGatewayService;
//	@Test
//	public void addUsersTest(){
//		Mono<AddUserDetails> userDtoMono = Mono.just(new AddUserDetails("102","mobile","","","",""));
//		when(apiGatewayService.addUser(userDtoMono).thenReturn(userDtoMono));
//
//		webTestClient.post().uri("/products/save")
//				.body(Mono.just(userDtoMono),ApiResponse.class)
//				.exchange()
//				.expectStatus().isOk();//200
//
//	}

}
