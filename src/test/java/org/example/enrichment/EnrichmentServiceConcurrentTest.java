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
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

public class EnrichmentServiceConcurrentTest {
  private EnrichmentService enrichmentService;

  @BeforeEach
  public void setUp() {
    UserRepositoryInterface userRepository = new InMemoryUserRepository();
    userRepository.updateUserByMsisdn("mystery", new User("Secret", "User"));
    this.enrichmentService = new EnrichmentService(List.of(new EnrichByMsisdn(userRepository)));
  }

  @Test
  public void testConcurrencyEnrichment() throws InterruptedException {
    ExecutorService executorService = Executors.newFixedThreadPool(5);
    CountDownLatch latch = new CountDownLatch(5);
    List<Message> enrichmentResults = new CopyOnWriteArrayList<>();

    for (int i = 0; i < 5; i++) {
      executorService.submit(() -> {
        Map<String, String> input = new HashMap<>();
        input.put("action", "secret_action");
        input.put("page", "XXX");
        input.put("msisdn", "mystery");

        Message message = new Message(input, Message.EnrichmentType.MSISDN);
        Message enrichedMessage = enrichmentService.enrich(message);
        enrichmentResults.add(enrichedMessage);
        latch.countDown();
      });
    }

    latch.await();
    executorService.shutdown();

    for (Message enrichedMessage : enrichmentResults) {
      Map<String, String> enrichedContent = enrichedMessage.getContent();
      assertEquals("Secret", enrichedContent.get("firstName"));
      assertEquals("User", enrichedContent.get("lastName"));
    }
  }
}