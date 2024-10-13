package org.example.myArray;

import org.example.myArray.exceptions.EmptyArrayException;
import org.example.myArray.exceptions.MyIndexOutOfBoundsException;
import org.example.myArray.exceptions.NullArgumentException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.max;

/**
 * Реализация кастомного ArrayList
 *
 * @param <T> generic
 */
public class CustomArrayList<T> implements CustomArrayListMethods<T> {

  /**
   * Массив object'ов, по сути являющихся элементами типа <T>
   */
  private Object[] array;

  /**
   * Вместимость массива
   */
  private int capacity;

  /**
   * Указатель на следующий свободный элемент
   */
  private int currentIndex;

  /**
   * Конструктор, копирующий элементы массива objects
   *
   * @param objects исходный массив
   */
  public CustomArrayList(Object[] objects) {
    capacity = max(objects.length, 16);
    System.arraycopy(objects, 0, array = new Object[capacity], 0, objects.length);
    currentIndex = objects.length;
  }

  public CustomArrayList(ArrayList<Integer> list) {
    capacity = max(list.size(), 16);
    currentIndex = list.size();
    for (int i = 0; i < currentIndex; i++) {
      array[i] = list.get(i);
    }
    //System.arraycopy(list, 0, array = new Object[capacity], 0, list.size());
  }

  /**
   * Default конструктор
   */
  public CustomArrayList() {
    capacity = 16;
    array = new Object[this.capacity];
    currentIndex = 0;
  }

  /**
   * Конструктор, создающий массив из 1 элемента
   *
   * @param value исходное значение
   */
  public CustomArrayList(T value) {
    this.capacity = 16;
    this.array = new Object[this.capacity];
    array[0] = value;
    this.currentIndex = 1;
  }

  public CustomArrayList(CustomArrayList<T> arr) {

    this.capacity = arr.getCapacity();
    this.array = new Object[this.capacity];
    this.currentIndex = arr.getCurrentIndex();
    for (int i = 0; i < currentIndex; i++) {
      array[i] = arr.get(i);
    }
    //System.arraycopy(arr, 0, array, 0, arr.getCurrentIndex());
//    System.out.println("help");


  }

  public CustomArrayList(List<T> arr) {

    this.capacity = arr.size();
    this.array = new Object[this.capacity];
    this.currentIndex = arr.size();
    for (int i = 0; i < currentIndex; i++) {
      array[i] = arr.get(i);
    }
    //System.arraycopy(arr, 0, array, 0, arr.getCurrentIndex());
//    System.out.println("help");


  }

  /**
   * Метод, который динамически расширяет array,
   * увеличивая capacity
   */
  protected void expandArray() {
    capacity = capacity * 3 / 2 + 1;
    Object[] newArray = new Object[capacity];
    System.arraycopy(array, 0, newArray, 0, array.length);
    array = newArray;
  }

  /**
   * Метод, который динамически сжимает array,
   * уменьшая capacity
   */
  protected void shrinkArray() {
    capacity = (capacity - 1) * 2 / 3;
    Object[] newArray = new Object[capacity];
    System.arraycopy(array, 0, newArray, 0, newArray.length);
    array = newArray;
  }

  /**
   * Метод, добавляющий value в array
   *
   * @param value значение типа <T>
   * @throws NullArgumentException если передан аргумент типа null
   */
  @Override
  public void add(T value) {
    if (value == null) {
      throw new NullArgumentException("Array element cannot be null");
    }
    if (capacity == currentIndex) {
      expandArray();
    }
    array[currentIndex] = value;
    currentIndex++;
  }

  public void set(T value, int index) {
    if (value == null) {
      throw new NullArgumentException("Array element cannot be null");
    }
    if (index < 0 || index >= currentIndex) {
      throw new MyIndexOutOfBoundsException("Index is out of array bounds");
    }
    array[index] = value;
  }

  public ArrayList<Integer> makeArrayList() {
    ArrayList<Integer> result = new ArrayList<>(currentIndex);
    for (int i = 0; i < currentIndex; i++) {
//      result.set((Integer) array[i], i);
      result.add((Integer) array[i]);
    }
    //System.arraycopy(array, 0, result, 0, array.length);
    return result;
  }

  /**
   * Метод, возвращающий элемент array с индексом index
   *
   * @param index индекс элемента
   * @return значение типа <T>
   * @throws MyIndexOutOfBoundsException если элемента с индексом index
   *                                     нет в array
   */
  @Override
  public T get(int index) {
    if (index < 0 || index >= currentIndex) {
      throw new MyIndexOutOfBoundsException("Index is out of array bounds");
    }
    return (T) array[index];
  }

  /**
   * Метод возвращающий вместимость array
   *
   * @return capacity типа int
   */
  public int getCapacity() {
    return capacity;
  }

  public int getCurrentIndex() { return currentIndex; }

  /**
   * Метод, убирающий элемент с индексом index из array,
   * сдвигая элементы правее index влево
   *
   * @param index индекс элемента
   * @throws EmptyArrayException         если array пустой
   * @throws MyIndexOutOfBoundsException если элемента с индексом index
   *                                     нет в array
   */
  @Override
  public void remove(int index) {
    if (currentIndex == 0) {
      throw new EmptyArrayException("Cannot remove from an empty array");
    }
    if (index < 0 || index >= currentIndex) {
      throw new MyIndexOutOfBoundsException("Index is out of array bounds");
    }
    for (int i = index; i < currentIndex - 1; i++) {
      array[i] = array[i + 1];
    }
    currentIndex--;
    if (currentIndex == (capacity - 1) * 2 / 3 && capacity > 16) {
      shrinkArray();
    }
  }

  /**
   * Метод, выводящий array в консоль
   */
  public void printArray() {
    for (int i = 0; i < currentIndex; i++) {
      System.out.print(array[i] + " ");
    }
    System.out.println();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CustomArrayList<?> that = (CustomArrayList<?>) o;
    return capacity == that.capacity && currentIndex == that.currentIndex && Objects.deepEquals(array, that.array);
  }

  @Override
  public int hashCode() {
    return Objects.hash(Arrays.hashCode(array), capacity, currentIndex);
  }
}
