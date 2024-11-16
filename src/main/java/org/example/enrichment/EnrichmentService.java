package org.example.enrichment;

import org.example.Message;

import java.util.List;

public class EnrichmentService {
  private final List<EnrichmentInterface> enrichments;
  public EnrichmentService(List<EnrichmentInterface> enrichments) {
    this.enrichments = enrichments;
  }

  public synchronized Message enrich(Message message) throws IllegalArgumentException{
    if (message == null) {
      throw new IllegalArgumentException();
    }

    for (EnrichmentInterface enrichment : this.enrichments) {
      if (enrichment.type().equals(message.getEnrichmentType())) {
        return enrichment.enrich(message);
      }
    }

    return message;
  }
}
