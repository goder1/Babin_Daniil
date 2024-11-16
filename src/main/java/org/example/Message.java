package org.example;

import java.util.HashMap;
import java.util.Map;

public class Message {
  protected Map<String, String> content;
  protected EnrichmentType enrichmentType;

  public EnrichmentType getEnrichmentType() {
    return enrichmentType;
  }

  public Message(Map<String, String> content, EnrichmentType enrichmentType) {
    this.content = new HashMap<>(content);
    this.enrichmentType = enrichmentType;
  }

  public enum EnrichmentType {
    MSISDN
  }

  public void updateContent(String key, String value) {
    this.content.put(key, value);
  }

  public Map<String, String> getContent() {
    return content;
  }
}