package org.example.food;

public class WrongFoodException extends RuntimeException {
  public WrongFoodException(String message) {
    super(message);
  }
}
