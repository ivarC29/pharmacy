package com.allicaihub.pharmacy.service;

import com.allicaihub.pharmacy.persistence.document.Client;
import com.allicaihub.pharmacy.persistence.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Flux<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Mono<Client> findById(String id) {
        return clientRepository.findById(id);
    }

    public Mono<Client> findByEmail(String email) {
        return clientRepository.findFirstByClientEmail(email);
    }

    public Mono<Client> findByPhoneOrName(String cellphoneOrName) {
        return clientRepository.findAllByClientCellphoneOrClientName(cellphoneOrName);
    }

    public Mono<Client> save(Client client) {
        return clientRepository.save(client);
    }

    public Mono<Void> delete(String id) {
        return clientRepository.deleteById(id);
    }
}
