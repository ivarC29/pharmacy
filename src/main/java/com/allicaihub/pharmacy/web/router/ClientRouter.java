package com.allicaihub.pharmacy.web.router;

import com.allicaihub.pharmacy.web.handler.ClientHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration(proxyBeanMethods = false)
public class ClientRouter {

    @Bean
    public RouterFunction<ServerResponse> route(ClientHandler clientHandler) {
        return RouterFunctions.route()
                .path(
                        "api/v1/client", builder -> builder
                                .GET("/all",accept(MediaType.APPLICATION_JSON), clientHandler::findAll)
                                .GET("/{id}",accept(MediaType.APPLICATION_JSON), clientHandler::findById)
                                .GET("/email/{email}", accept(MediaType.APPLICATION_JSON), clientHandler::findByEmail)
                                .GET("/phone_or_name/{phone_or_name}", accept(MediaType.APPLICATION_JSON), clientHandler::findByPhoneOrName)
                                .POST("/add", accept(MediaType.APPLICATION_JSON), clientHandler::create)
                                .PUT("/{id}", accept(MediaType.APPLICATION_JSON), clientHandler::update)
                                .DELETE("/{id}", accept(MediaType.APPLICATION_JSON), clientHandler::delete)
                ).build();
//                .andRoute(GET("other"), req -> ServerResponse.ok()
//                        .body(Mono.just("Other route"), String.class));
    }

}
