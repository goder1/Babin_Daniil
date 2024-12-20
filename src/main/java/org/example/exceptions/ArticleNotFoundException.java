package org.example.exceptions;

public class ArticleNotFoundException extends RuntimeException {
  public ArticleNotFoundException(String message, EntityNotFoundException e) {
    super(message);
  }
}
