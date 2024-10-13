package org.example.sort_files.sorts;

import org.example.myArray.CustomArrayList;
import org.example.myArray.CustomArrayListWrap;
import org.example.sort_files.sortInterface.SorterInterface;

public abstract class Sort implements SorterInterface {
  protected CustomArrayListWrap<Integer> arr;
  protected int elementsCountLimit;

  public Sort(int limit) {
    this.elementsCountLimit = limit;
  }

  protected void wrap(CustomArrayList<Integer> list) {
    arr = new CustomArrayListWrap<Integer>(list);
  }

  @Override
  public void setElementsCountLimit(int limit) {
    if (limit <= 0) {
      throw new IllegalArgumentException("В списке должен быть хотя бы 1 элемент");
    }
    this.elementsCountLimit = limit;
  }

  @Override
  public int getElementsCountLimit() {
    return elementsCountLimit;
  }
}
