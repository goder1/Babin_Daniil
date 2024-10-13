package org.example;

import org.example.myArray.CustomArrayList;
import org.example.myArray.exceptions.EmptyArrayException;
import org.example.myArray.exceptions.MyIndexOutOfBoundsException;
import org.example.myArray.exceptions.NullArgumentException;
import org.example.sort_files.sortInterface.SortManager;
import org.example.sort_files.sortInterface.SortTypes;
import org.example.sort_files.sorts.BubbleSort;
import org.example.sort_files.sorts.CollectionsSort;

import java.util.Arrays;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    Integer[] i = {1, 100, 10, 42, 69, 13, 7, 52, 228, 1337};
    Integer[] i2 = {1000, 1};
    CustomArrayList<Integer> arr = new CustomArrayList<>(i);
    CustomArrayList<Integer> arr2 = new CustomArrayList<>(i2);

    CollectionsSort CollectionsSortMax8 = new CollectionsSort(8);
    CollectionsSort CollectionsSortMax16 = new CollectionsSort(16);
    BubbleSort bubbleSortMax8 = new BubbleSort(8);
    BubbleSort bubbleSortMax32 = new BubbleSort(32);

    SortManager manager = new SortManager(Arrays.asList(CollectionsSortMax8, CollectionsSortMax16, bubbleSortMax8, bubbleSortMax32));
    CustomArrayList<Integer> sortedList;
    CustomArrayList<Integer> sortedList2;
    try {
      sortedList = manager.sort(arr, SortTypes.MERGE);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    try {
      sortedList2 = manager.sort(arr2, SortTypes.BUBBLE);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    sortedList.printArray();
    sortedList2.printArray();
    sortedList.makeArrayList();

  }
}