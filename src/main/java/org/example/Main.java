package org.example;

import org.example.myArray.CustomArrayList;
import org.example.myArray.exceptions.EmptyArrayException;
import org.example.myArray.exceptions.MyIndexOutOfBoundsException;
import org.example.myArray.exceptions.NullArgumentException;

public class Main {
  public static void main(String[] args) {
    CustomArrayList<Integer> arr1 = new CustomArrayList<>();
    CustomArrayList<String> arr2 = new CustomArrayList<>("aba");
    Double[] d = {1.2, 2.5, 6.0, 7.8, 1.0, 5.9, 6.2, 1.2, 1.2, 0.7};
    CustomArrayList<Double> arr3 = new CustomArrayList<>(d);

    arr1.add(1);
    arr1.add(2);
    arr1.add(3);
    arr1.printArray();
    arr1.remove(1);
    arr1.printArray();

    arr2.printArray();
    System.out.println("0 element: " + arr2.get(0));
    arr2.remove(0);
    arr2.printArray();

    System.out.println("Capacity: " + arr3.getCapacity());
    arr3.printArray();
    for (int i = 0; i < 7; i++) {
      arr3.add(1.1);
    }
    System.out.println("Capacity: " + arr3.getCapacity());
    arr3.printArray();
    arr3.remove(2);
    System.out.println("Capacity: " + arr3.getCapacity());
    arr3.printArray();

    try {
      arr3.add(null);
    } catch (NullArgumentException e) {
      System.out.println("NullArgumentException");
    }

    try {
      arr3.get(-1);
    } catch (MyIndexOutOfBoundsException e) {
      System.out.println("MyIndexOutOfBoundsException");
    }

    arr1.remove(0);
    arr1.remove(0);

    try {
      arr1.remove(0);
    } catch (EmptyArrayException e) {
      System.out.println("EmptyArrayException");
    }

  }
}