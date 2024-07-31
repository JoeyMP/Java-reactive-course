package com.react.project.webflux.sec06.config;

import com.react.project.webflux.sec06.exception.ApplicationException;
import com.react.project.webflux.sec06.exception.CustomerNotFoundException;
import com.react.project.webflux.sec06.exception.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfiguration {

    @Autowired
    private CustomerRequestHandler customerRequestHandler;

    @Autowired
    private ApplicationExceptionHandler exceptionHandler;

    @Bean
    public RouterFunction<ServerResponse> customerRoutes() {
        return RouterFunctions.route()
                .GET("/customer", this.customerRequestHandler::allCustomer)
                .GET("/customer/paginated", this.customerRequestHandler::paginatedCustomer)
                .GET("/customer/{id}", this.customerRequestHandler::getCustomer)
                //.GET("/customer/{id}", RequestPredicates.path("*/1?"), this.customerRequestHandler::getCustomer)
                .POST("/customer", this.customerRequestHandler::saveCustomer)
                .PUT("/customer/{id}", this.customerRequestHandler::updateCustomer)
                .DELETE("/customer/{id}", this.customerRequestHandler::deleteCustomer)
                .onError(CustomerNotFoundException.class, this.exceptionHandler::handlerException)
                .onError(InvalidInputException.class, this.exceptionHandler::handlerException)
                .build();
    }


/*    @Bean
    public RouterFunction<ServerResponse> customerRoutes1() {
        return RouterFunctions.route()
                .path("customer", this::customerRoutes2)
*//*                .GET("/customer", this.customerRequestHandler::allCustomer)
                .GET("/customer/paginated", this.customerRequestHandler::paginatedCustomer)
                .GET("/customer/{id}", this.customerRequestHandler::getCustomer)*//*
                .POST("/customer", this.customerRequestHandler::saveCustomer)
                .PUT("/customer/{id}", this.customerRequestHandler::updateCustomer)
                .DELETE("/customer/{id}", this.customerRequestHandler::deleteCustomer)
                .onError(CustomerNotFoundException.class, this.exceptionHandler::handlerException)
                .onError(InvalidInputException.class, this.exceptionHandler::handlerException)
                .build();
    }

    //@Bean
    private RouterFunction<ServerResponse> customerRoutes2() {
        return RouterFunctions.route()
                .GET("/paginated", this.customerRequestHandler::paginatedCustomer)
                .GET("/{id}", this.customerRequestHandler::getCustomer)
                .GET(this.customerRequestHandler::allCustomer)
                .build();
    }*/


}
