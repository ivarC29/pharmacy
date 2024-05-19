package com.allicaihub.pharmacy.web.handler;

import com.allicaihub.pharmacy.persistence.document.Client;
import com.allicaihub.pharmacy.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.ServerResponse.accepted;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class ClientHandler {

    private final ClientService clientService;

    private final Mono<ServerResponse> response404 = ServerResponse.notFound().build();
    private final Mono<ServerResponse> response406 = ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).build();
    private final Mono<ServerResponse> response422 = ServerResponse.unprocessableEntity().build();

    @Autowired
    public ClientHandler(ClientService clientService) {
        this.clientService = clientService;
    }

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(clientService.getAllClients(), Client.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        String id = request.pathVariable("id");

        return clientService.findById(id)
                .flatMap( client ->
                            ok().contentType(MediaType.APPLICATION_JSON)
                                    .body(fromValue(client)))
                .switchIfEmpty(response404);
    }

    public Mono<ServerResponse> findByEmail(ServerRequest request) {
        String email = request.pathVariable("email");

        return clientService.findByEmail(email)
                .flatMap( client ->
                        ok().contentType(MediaType.APPLICATION_JSON)
                                .body(fromValue(client)))
                .switchIfEmpty(response404);
    }

    public Mono<ServerResponse> findByPhoneOrName(ServerRequest request) {
        String phoneOrName = request.pathVariable("phone_or_name");

        return clientService.findByPhoneOrName(phoneOrName)
                .flatMap( client ->
                        ok().contentType(MediaType.APPLICATION_JSON)
                                .body(fromValue(client)))
                .switchIfEmpty(response404);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<Client> clientToSave = request.bodyToMono(Client.class);

        return clientToSave
                .flatMap(client -> clientService.save(client)
                        .flatMap(savedClient -> accepted()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(fromValue(savedClient))))
                .switchIfEmpty(response406);
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Mono<Client> clientToSave = request.bodyToMono(Client.class);
        String id = request.pathVariable("id");

        Mono<Client> clientToUpdate = clientToSave.flatMap( client ->
                clientService.findById(id)
                        .flatMap(oldClient -> {
                            oldClient.setClientName(client.getClientName());
                            oldClient.setClientEmail(client.getClientEmail());
                            oldClient.setClientCellphone(client.getClientCellphone());
                            return clientService.save(oldClient);
                        }));

        return clientToUpdate.flatMap(client ->
                accepted()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(client)))
                .switchIfEmpty(response404);
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<Void> deletedClient = clientService.delete(id);

        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(deletedClient, Void.class);
    }

}
