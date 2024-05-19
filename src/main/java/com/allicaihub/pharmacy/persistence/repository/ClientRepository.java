package com.allicaihub.pharmacy.persistence.repository;

import com.allicaihub.pharmacy.persistence.document.Client;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ClientRepository extends ReactiveMongoRepository<Client, String> {

    Mono<Client> findFirstByClientEmail(String clientEmail);
    @Query("{ '$or' : [ {'clientCellphone' : ?0}, {'clientName' : ?0} ] }")
    Mono<Client> findAllByClientCellphoneOrClientName(String cellphoneOrName);

}
