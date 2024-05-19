package com.allicaihub.pharmacy.persistence.repository;

import com.allicaihub.pharmacy.persistence.document.Client;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClientRepositoryTest {

    @Autowired
    private ClientRepository repository;

    @Autowired
    private ReactiveMongoOperations mongoOperations;

    @BeforeAll
    void setUp() {
        Client client1 = Client.builder()
                .clientName("Client Test One")
                .clientEmail("client1@allicaihub.com")
                .clientCellphone("999999999")
                .build();

        Client client2= Client.builder()
                .clientName("Client Test Two")
                .clientEmail("client2@allicaihub.com")
                .clientCellphone("888888888")
                .build();

        Client client3 = Client.builder()
                .clientName("Client Test Three")
                .clientEmail("client3@allicaihub.com")
                .clientCellphone("777777777")
                .build();

        StepVerifier.create(repository.save(client1).log())
                .expectSubscription().expectNextCount(1)
                .verifyComplete();

        StepVerifier.create(repository.save(client2).log())
                .expectSubscription().expectNextCount(1)
                .verifyComplete();

        StepVerifier.create(repository.save(client3).log())
                .expectNextMatches(client -> (client.getClientId() != null))
                .verifyComplete();

    }

    @DisplayName("Test for get all clients.")
    @Test
    @Order(1)
    void testGetAll() {

        StepVerifier.create(repository.findAll().log())
                .expectSubscription()
                .expectNextCount(3)
                .verifyComplete();
    }

    @DisplayName("Test get client by id.")
    @Test
    @Order(2)
    void testGetById() {
        StepVerifier.create(repository.findById(UUID.randomUUID().toString()))
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();
    }

    @DisplayName("")
    @Test
    @Order(3)
    void findFirstByClientEmail() {
        StepVerifier.create(repository.findFirstByClientEmail("client2@allicaihub.com").log())
                .expectSubscription()
                .expectNextMatches(client -> client.getClientEmail().equals("client2@allicaihub.com"))
                .verifyComplete();
    }

    @DisplayName("")
    @Test
    @Order(4)
    void findAllByClientCellphoneOrClientName() {
        StepVerifier.create(repository.findAllByClientCellphoneOrClientName("888888888").log())
                .expectSubscription()
                .expectNextMatches(client -> client.getClientCellphone().equals("888888888"))
                .verifyComplete();

        StepVerifier.create(repository.findAllByClientCellphoneOrClientName("Client Test Three").log())
                .expectSubscription()
                .expectNextMatches(client -> client.getClientName().equals("Client Test Three"))
                .verifyComplete();
    }

    @DisplayName("Test for update client")
    @Test
    @Order(5)
    void updateClient() {
        Mono<Client> clientUpdated = repository.findFirstByClientEmail("client1@allicaihub.com")
                .map( client -> {
                    client.setClientCellphone("666666666");
                    return client;
                }).flatMap(repository::save);

        StepVerifier.create(clientUpdated.log())
                .expectSubscription()
                .expectNextMatches(client -> client.getClientCellphone().equals("666666666"))
                .verifyComplete();
    }

    @DisplayName("Test for delete client")
    @Test
    @Order(6)
    void deleteClient() {
        Mono<Void> clientDeleted = repository.findFirstByClientEmail("client3@allicaihub.com")
                .flatMap(client -> repository.deleteById(client.getClientId()));

        StepVerifier.create(clientDeleted)
                        .expectSubscription()
                                .verifyComplete();

        StepVerifier.create(repository.findAll().log())
                .expectSubscription()
                .expectNextCount(2)
                .verifyComplete();
    }

    @AfterAll
    void tearDown() {
        Mono<Void> clientDeleted = repository.deleteAll();
        StepVerifier.create(clientDeleted.log())
                .expectSubscription()
                .verifyComplete();
    }
}