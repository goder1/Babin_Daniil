package org.example.sort_files.sortInterface;

import java.util.List;

import org.example.myArray.CustomArrayList;
import org.example.sort_files.sorts.Sort;

public class SortManager {
  List<Sort> listOfSorters;

  public SortManager(List<Sort> listOfSorters) {
    this.listOfSorters = listOfSorters;
  }

  public CustomArrayList<Integer> sort(CustomArrayList<Integer> list, SortTypes type) throws Exception {
    boolean foundSort = false;
    for (var sorter: listOfSorters) {
      if (sorter.type().equals(type)) {
        foundSort = true;
        try {
          return sorter.sort(list);
        } catch (Exception e) {
          System.out.println("Неподходящаяя сортировка " + sorter + ": " + e);
        }
      }
    }
    if (!foundSort) {
      throw new Exception("Не найдена подходящая сортировка");
    }
    return list;
  }
}
