package org.example.myArray.exceptions;

/**
 * Исключение, которое появляется, когда из массива берётся элемент с индексом,
 * которого не существует
 */
public class MyIndexOutOfBoundsException extends RuntimeException {
  public MyIndexOutOfBoundsException(String message) {
    super(message);
  }
}
