package org.example.sort_files.tests;

import org.example.myArray.CustomArrayList;
import org.example.sort_files.sortInterface.SortTypes;
import org.example.sort_files.sorts.BubbleSort;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CustomArrayListTest {
  @Test
  void add() throws Exception {
    CustomArrayList<Integer> list = new CustomArrayList<>();
    list.add(1);
    CustomArrayList<Integer> result = new CustomArrayList<>(1);
    assertEquals(list.makeArrayList(), result.makeArrayList());
  }

  @Test
  void makeArrayList() {
    ArrayList<Integer> list = new ArrayList<>();
    list.add(1);
    list.add(2);
    list.add(3);
    CustomArrayList<Integer> Customlist = new CustomArrayList<>(list);
    assertEquals(Customlist.makeArrayList(), list);
  }

  @Test
  void remove() {
    ArrayList<Integer> list = new ArrayList<>();
    list.add(1);
    list.add(2);
    list.add(3);
    CustomArrayList<Integer> Customlist = new CustomArrayList<>(list);
    Customlist.remove(0);
    list.remove(0);
    assertEquals(Customlist.makeArrayList(), list);
  }
}
