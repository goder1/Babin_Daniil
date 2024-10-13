package org.example.sort_files.tests;

import org.example.myArray.CustomArrayList;
import org.example.sort_files.sortInterface.SortTypes;
import org.example.sort_files.sorts.BubbleSort;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BubbleSortTest {
  @Test
  void sort() throws Exception {
    List<Integer> list = Arrays.asList(5, 9, 0, -2);
    CustomArrayList<Integer> Customlist = new CustomArrayList<>(list);
    List<Integer> result = Arrays.asList(-2, 0, 5, 9);
    BubbleSort sorter = new BubbleSort(10);
    CustomArrayList<Integer> CustomResult = new CustomArrayList<>(result);
    CustomArrayList<Integer> sortedList = sorter.sort(Customlist);
    assertEquals(sortedList.makeArrayList(), CustomResult.makeArrayList());
  }

  @Test
  void sortWithException() {
    List<Integer> list = Arrays.asList(1, 1, 1, 1, 1, 1, 1);
    CustomArrayList<Integer> Customlist = new CustomArrayList<>(list);
    BubbleSort sorter = new BubbleSort(4);
    assertThrows(Exception.class, () -> sorter.sort(Customlist));
  }

  @Test
  void type() {
    SortTypes answer = new BubbleSort(5).type();
    assertEquals(answer, SortTypes.BUBBLE);
  }
}
