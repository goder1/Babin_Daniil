package org.example.myArray;

/**
 * Интерфейс для CustomArrayList
 *
 * @param <T> работает со значениями generic типа <T>
 */
public interface CustomArrayListMethods<T> {
  public void add(T elem);

  public T get(int index);

  public void remove(int index);
}
