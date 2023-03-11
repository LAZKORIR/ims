//package com.ims.apigateway;
//
//import com.ims.apigateway.dto.ApiRequest;
//import com.ims.apigateway.dto.ApiResponse;
//import com.ims.apigateway.model.AddUserDetails;
//import com.ims.apigateway.service.ApiGatewayService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.reactive.server.WebTestClient;
//import reactor.core.publisher.Mono;
//
//import java.util.UUID;
//
//import static org.mockito.Mockito.when;
//
//@ExtendWith(SpringExtension.class)
//@WebFluxTest(ApiGatewayApplicationTests.class)
//public class ApiGatewayApplicationTests {
//
//	private final String TEST_EMAIL = "test@gmail.com";
//
//	@Autowired
//	private WebTestClient webClient;
//
//	@MockBean
//	private ApiGatewayService apiGatewayService;
//
//	@Test
//	@WithMockUser(username = TEST_EMAIL)
//	public void shouldGetUser() {
//
//		ApiRequest apiRequest = new ApiRequest();
//		apiRequest.setRequestRefID(UUID.randomUUID().toString());
//		apiRequest.setAmount("200");
//		apiRequest.setFirstname("Lazz");
//		apiRequest.setLastname("Korir");
//		apiRequest.setMsisdn("254723846453");
//		apiRequest.setSourceSystem("Testing app");
//		apiRequest.setEmail("lazaruskorir@gmail.com");
//
//		when(apiGatewayService.addUser(apiRequest)).thenReturn(Mono.just(user));
//
//		webClient
//				.get().uri("/api/v1/addUser")
//				.exchange()
//				.expectStatus()
//				.isOk()
//				.expectBody(ApiRequest.class);
//	}
//
//	@Test
//	@WithMockUser(username = TEST_EMAIL)
//	public void shouldGetUserFail() {
//
//		when(userService.getUser(TEST_EMAIL)).thenThrow(new UserNotFoundException());
//
//		webClient
//				.get().uri("/user")
//				.exchange()
//				.expectStatus()
//				.isForbidden()
//				.expectBody()
//				.jsonPath("code").isEqualTo(USER_NOT_FOUND.getCode());
//	}
//}
//
