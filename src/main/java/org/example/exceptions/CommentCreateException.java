package org.example.exceptions;

public class CommentCreateException extends RuntimeException {
  public CommentCreateException(String message, EntityNotFoundException e) {
    super(message);
  }
}
