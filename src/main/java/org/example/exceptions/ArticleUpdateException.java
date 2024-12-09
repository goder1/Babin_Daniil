package org.example.exceptions;

public class ArticleUpdateException extends RuntimeException {
  public ArticleUpdateException(String message, EntityNotFoundException e) {
    super(message);
  }
}
