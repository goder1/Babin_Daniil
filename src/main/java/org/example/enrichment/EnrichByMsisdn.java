package org.example.enrichment;

import org.example.Message;
import org.example.user.User;
import org.example.user.UserRepositoryInterface;

public class EnrichByMsisdn implements EnrichmentInterface {
  private final UserRepositoryInterface userRepository;

  public EnrichByMsisdn(UserRepositoryInterface userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public Message.EnrichmentType type() {
    return Message.EnrichmentType.MSISDN;
  }

  @Override
  public Message enrich(Message message) {
    Message resultMessage = new Message(message.getContent(), message.getEnrichmentType());

    String msisdn = resultMessage.getContent().get("msisdn");
    if (msisdn != null) {
      User user = userRepository.findByMsisdn(msisdn);
      if (user != null) {
        resultMessage.updateContent("firstName", user.firstName());
        resultMessage.updateContent("lastName", user.lastName());
      }
    }

    return resultMessage;
  }
}