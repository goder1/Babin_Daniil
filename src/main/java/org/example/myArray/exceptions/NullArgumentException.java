package org.example.myArray.exceptions;

/**
 * Исключение, которое появляется, когда в массив передаётся значение типа null
 */
public class NullArgumentException extends RuntimeException {
  public NullArgumentException(String message) {
    super(message);
  }
}
