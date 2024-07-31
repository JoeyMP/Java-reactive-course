package com.react.project.webflux.sec07;

import com.react.project.webflux.sec07.dto.CalculatorResponse;
import com.react.project.webflux.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec05ErrorResponseTest extends AbstractWebClient {

    private static final Logger log = LoggerFactory.getLogger(Lec05ErrorResponseTest.class);
    private final WebClient client = createWebClient();

    @Test
    public void handlingResponse() {
        this.client.get()
                .uri("/lec05/calculator/{first}/{second}", 10, 5)
                .header("operation", "+")
                //.header("operation", "@")
                .retrieve()//return ResponseSpec(response body), to get headers and response status you need to use .toEntity
                .bodyToMono(CalculatorResponse.class)
                //.onErrorReturn(new CalculatorResponse(0, 0, null, 0))
                .doOnError(WebClientResponseException.class, ex -> log.info("{}", ex.getResponseBodyAs(ProblemDetail.class)))
                .onErrorReturn(WebClientResponseException.InternalServerError.class, new CalculatorResponse(0, 0, null, 0))
                .onErrorReturn(WebClientResponseException.BadRequest.class, new CalculatorResponse(0, 0, null, -1))
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void exchange() {
        this.client.get()
                .uri("/lec05/calculator/{first}/{second}", 10, 5)
                .header("operation", "+")
                .exchangeToMono(this::decode)//return ClientResponse, to get more control about response like cookies, headers, etc
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    private Mono<CalculatorResponse> decode(ClientResponse clientResponse) {
        //clientResponse.cookies();
        //clientResponse.headers();
        log.info("status code: {}", clientResponse.statusCode());
        if (clientResponse.statusCode().is4xxClientError()) {
            return clientResponse.bodyToMono(ProblemDetail.class)
                    .doOnNext(pw -> log.info("{}", pw))
                    .then(Mono.empty());
        }
        return clientResponse.bodyToMono(CalculatorResponse.class);
    }


}
