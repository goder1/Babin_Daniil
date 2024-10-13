package org.example.sort_files.sorts;

import org.example.myArray.CustomArrayList;
import org.example.sort_files.sortInterface.SortTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionsSort extends Sort{
  public CollectionsSort(int limit) {
    super(limit);
  }

  @Override
  public CustomArrayList<Integer> sort(CustomArrayList<Integer> list) throws Exception {
    if (list.getCurrentIndex() > elementsCountLimit) {
      throw new Exception("Превышен лимит размера для сортировки пузырьком, должно быть не больше "
              + elementsCountLimit + " элементов, в переданном списке - " + arr.size());
    }

    CustomArrayList<Integer> tmp = new CustomArrayList<>(list);
    for (int i = 0; i < list.getCurrentIndex(); i++) {
      tmp.set(list.get(i), i);
    }

    ArrayList<Integer> tmp2 = new ArrayList<Integer>();

    for (int i = 0; i < tmp.getCurrentIndex(); i++) {
      tmp2.add(tmp.get(i));
    }
    Collections.sort(tmp2);
    CustomArrayList<Integer> result = new CustomArrayList<Integer>();
    for (int i = 0; i < tmp.getCurrentIndex(); i++) {
      result.add(tmp2.get(i));
    }

    return result;
  }

  @Override
  public SortTypes type() {
    return SortTypes.MERGE;
  }
}
