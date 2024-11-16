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
    userRepository.updateUserByMsisdn("goodmorning", new User("Daniil", "Babin"));
    EnrichmentService enrichmentService = new EnrichmentService(List.of(new EnrichByMsisdn(userRepository)));

    Message message = new Message(
            Map.of(
                    "action", "insaneEnrichmentProcess",
                    "page", "myPage",
                    "msisdn", "goodmorning"
            ),
            Message.EnrichmentType.MSISDN
    );
    System.out.println("Message: ");
    message.content.forEach((key, value) -> System.out.println(key + ": " + value));
    System.out.println();
    Message result = enrichmentService.enrich(message);
    System.out.println("Enriched message: ");
    result.content.forEach((key, value) -> System.out.println(key + ": " + value));
  }
}