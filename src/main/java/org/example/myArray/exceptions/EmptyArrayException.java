package org.example.myArray.exceptions;

/**
 * Исключение, которое появляется, когда из пустого массива удаляется элемент
 */
public class EmptyArrayException extends RuntimeException {
  public EmptyArrayException(String message) {
    super(message);
  }
}
