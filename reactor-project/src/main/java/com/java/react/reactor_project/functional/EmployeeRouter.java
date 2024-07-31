package com.java.react.reactor_project.functional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class EmployeeRouter {

    @Bean
    public RouterFunction<ServerResponse> employeeRoute(EmployeeHandler employeeHandler) {
        return RouterFunctions
                .route(GET("/functional/employee"), employeeHandler::getAll)
                .andRoute(GET("functional/employee/{id}"), employeeHandler::getById)
                .andRoute(GET("functional/employee/email/{email}"), employeeHandler::getByEmail)
                .andRoute(POST("functional/employee"), employeeHandler::save)
                .andRoute(PUT("functional/employee/{id}"), employeeHandler::update)
                .andRoute(DELETE("functional/employee/{id}"), employeeHandler::delete);
    }
}
