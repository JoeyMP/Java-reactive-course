package com.react.project.webflux.sec01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("traditional")
public class TraditionalWebController {

    private static final Logger log = LoggerFactory.getLogger(TraditionalWebController.class);
    private final RestClient restClient = RestClient.builder().baseUrl("http://localhost:7070").build();

    @GetMapping("products")
    public List<Product> getProducts() {
        var list = restClient.get()
                .uri("/demo01/products")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Product>>() {
                });

        log.info("receive response {}", list);

        return list;
    }

    //This is not a reactive process
    @GetMapping("products2")
    public Flux<Product> getProducts2() {
        //synchronous and blocking code
        var list = restClient.get()
                .uri("/demo01/products")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Product>>() {
                });

        log.info("receive response {}", list);
        //just this part is reactive
        return Flux.fromIterable(list);
    }
}
