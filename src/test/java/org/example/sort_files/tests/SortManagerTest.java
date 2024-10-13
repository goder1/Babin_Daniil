package org.example.sort_files.tests;

import org.example.myArray.CustomArrayList;
import org.example.sort_files.sortInterface.SortManager;
import org.example.sort_files.sortInterface.SortTypes;
import org.example.sort_files.sorts.BubbleSort;
import org.example.sort_files.sorts.CollectionsSort;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SortManagerTest {

  @Test
  void sort() throws Exception {
    CollectionsSort collectionsSortMax5 = new CollectionsSort(5);
    CollectionsSort collectionsSortMax16 = new CollectionsSort(16);
    BubbleSort bubbleSortMax5 = new BubbleSort(5);
    BubbleSort bubbleSortMax32 = new BubbleSort(32);

    SortManager manager = new SortManager(Arrays.asList(collectionsSortMax5, collectionsSortMax16, bubbleSortMax5, bubbleSortMax32));

    List<Integer> list = Arrays.asList(12, 4, -2, 5, 0, 6, 0, 1, 8);
    List<Integer> result = Arrays.asList(-2, 0, 0, 1, 4, 5, 6, 8, 12);
    CustomArrayList<Integer> CustomResult = new CustomArrayList<>(result);
    CustomArrayList<Integer> Customlist = new CustomArrayList<>(list);
    CustomArrayList<Integer> sortedList = manager.sort(Customlist, SortTypes.MERGE);

    assertEquals(sortedList.makeArrayList(), CustomResult.makeArrayList());
  }
}