package com.allicaihub.pharmacy.persistence.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "client")
public class Client {

    @Id
    private String                    clientId;
    private String                  clientName;
    private String                  clientEmail;
    private String                  clientCellphone;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(clientId, client.clientId) && Objects.equals(clientName, client.clientName) && Objects.equals(clientEmail, client.clientEmail) && Objects.equals(clientCellphone, client.clientCellphone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, clientName, clientEmail, clientCellphone);
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientId=" + clientId +
                ", clientName='" + clientName + '\'' +
                ", clientEmail='" + clientEmail + '\'' +
                ", clientCellphone='" + clientCellphone + '\'' +
                '}';
    }
}
