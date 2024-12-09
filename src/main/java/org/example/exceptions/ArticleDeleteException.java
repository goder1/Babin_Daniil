package org.example.exceptions;

public class ArticleDeleteException extends RuntimeException {
  public ArticleDeleteException(String message, EntityNotFoundException e) {
    super(message);
  }
}
