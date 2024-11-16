package org.example.enrichment;

import org.example.Message;

public interface EnrichmentInterface {
  Message.EnrichmentType type();
  Message enrich(Message message);
}
