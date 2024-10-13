package org.example.sort_files.sortInterface;

import org.example.myArray.CustomArrayList;

public interface SorterInterface {
  CustomArrayList<Integer> sort(CustomArrayList<Integer> list) throws Exception;

  void setElementsCountLimit(int limit);

  int getElementsCountLimit();

  SortTypes type();
}
