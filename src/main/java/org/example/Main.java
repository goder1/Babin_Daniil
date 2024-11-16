package org.example;

import org.example.enrichment.EnrichByMsisdn;
import org.example.enrichment.EnrichmentService;
import org.example.user.InMemoryUserRepository;
import org.example.user.User;
import org.example.user.UserRepositoryInterface;

import java.util.List;
import java.util.Map;

public class Main {
  public static void main(String[] args) {
    UserRepositoryInterface userRepository = new InMemoryUserRepository();
    userRepository.updateUserByMsisdn("88005553535", new User("Ivan", "Sidorov"));
    EnrichmentService enrichmentService = new EnrichmentService(List.of(new EnrichByMsisdn(userRepository)));

    Message message = new Message(
            Map.of(
                    "action", "button_click",
                    "page", "book_card",
                    "msisdn", "88005553535"
            ),
            Message.EnrichmentType.MSISDN
    );
    System.out.println("Original message: ");
    message.content.forEach((key, value) -> System.out.println(key + ":" + value));
    System.out.println("------------------");
    Message result = enrichmentService.enrich(message);
    System.out.println("Enriched message: ");
    result.content.forEach((key, value) -> System.out.println(key + ":" + value));
    System.out.println("------------------");
  }
}