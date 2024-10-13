package org.example.sort_files.tests;

import org.example.myArray.CustomArrayList;
import org.example.sort_files.sortInterface.SortTypes;
import org.example.sort_files.sorts.CollectionsSort;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CollectionsSortTest {
  Random rand = new Random();

  @Test
  void setElementsCountLimit() {
    CollectionsSort sorter = new CollectionsSort(5);
    int n = rand.nextInt(100);
    sorter.setElementsCountLimit(n);
    assertEquals(n, sorter.getElementsCountLimit());
  }

  @Test
  void setElementsCountLimitWithException() {
    CollectionsSort sorter = new CollectionsSort(17);
    int n = rand.nextInt(-100, 0);
    assertThrows(IllegalArgumentException.class, () -> sorter.setElementsCountLimit(n));
  }

  @Test
  void getElementsCountLimit() {
    int n = rand.nextInt(100);
    CollectionsSort sorter = new CollectionsSort(n);
    assertEquals(n, sorter.getElementsCountLimit());
  }

  @Test
  void sort() throws Exception {
    List<Integer> list = Arrays.asList(1, -2, 5, 0, 9, 3, -7, 2);
    List<Integer> result = Arrays.asList(-7, -2, 0, 1, 2, 3, 5, 9);
    CustomArrayList<Integer> CustomResult = new CustomArrayList<>(result);
    CollectionsSort sorter = new CollectionsSort(32);
    CustomArrayList<Integer> Customlist = new CustomArrayList<>(list);
    CustomArrayList<Integer> sortedList = sorter.sort(Customlist);
    assertEquals(sortedList.makeArrayList(), CustomResult.makeArrayList());
  }

  @Test
  void type() {
    SortTypes answer = new CollectionsSort(5).type();
    assertEquals(answer, SortTypes.MERGE);
  }
}