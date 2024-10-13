package org.example.myArray;

import java.util.ArrayList;
import java.util.List;
import org.example.myArray.CustomArrayList;

public class CustomArrayListWrap<T> {
  private final CustomArrayList<T> array;

  public CustomArrayListWrap(CustomArrayList<T> list) {
    array = new CustomArrayList<T>(list);
  }

  public int size() {
    return array.getCurrentIndex();
  }
}