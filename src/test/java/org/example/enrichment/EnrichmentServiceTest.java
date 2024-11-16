package org.example.enrichment;

import org.example.Message;
import org.example.user.InMemoryUserRepository;
import org.example.user.User;
import org.example.user.UserRepositoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EnrichmentServiceTest {
  private EnrichmentService enrichmentService;

  @BeforeEach
  public void setUp() {
    UserRepositoryInterface userRepository = new InMemoryUserRepository();
    userRepository.updateUserByMsisdn("mystery", new User("Secret", "User"));
    this.enrichmentService = new EnrichmentService(List.of(new EnrichByMsisdn(userRepository)));
  }

  @Test
  public void testEnrichment() {
    Map<String, String> input = new HashMap<>();
    input.put("action", "secret_action");
    input.put("page", "XXX");
    input.put("msisdn", "mystery");

    Message message = new Message(input, Message.EnrichmentType.MSISDN);
    Message resultMessage = this.enrichmentService.enrich(message);

    Map<String, String> enrichedContent = resultMessage.getContent();
    assertEquals("Secret", enrichedContent.get("firstName"));
    assertEquals("User", enrichedContent.get("lastName"));
  }
}