package org.example.sort_files.sorts;

import org.example.myArray.CustomArrayList;
import org.example.sort_files.sortInterface.SortTypes;

public class BubbleSort extends Sort{
  public BubbleSort(int limit) {
    super(limit);
  }

  @Override
  public CustomArrayList<Integer> sort(CustomArrayList<Integer> list) throws Exception {

    if (list.getCurrentIndex() > elementsCountLimit) {
      throw new Exception("Превышен лимит размера для сортировки пузырьком, должно быть не больше "
              + elementsCountLimit + " элементов, в переданном списке - " + arr.size());
    }

    CustomArrayList<Integer> result = new CustomArrayList<>(list);
    for (int i = 0; i < list.getCurrentIndex(); i++) {
      result.set(list.get(i), i);
    }

    for (int i = 0; i < result.getCurrentIndex(); i++) {
      for (int j = i + 1; j < result.getCurrentIndex(); j++) {
        if (result.get(i) > result.get(j)) {
          Integer temp = result.get(i);
          result.set(result.get(j), i);
          result.set(temp, j);
        }
      }
    }
    return result;
  }

  @Override
  public SortTypes type() {
    return SortTypes.BUBBLE;
  }
}
