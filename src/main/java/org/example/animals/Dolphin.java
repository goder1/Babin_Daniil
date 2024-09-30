package org.example.animals;

import org.example.area.Water;
import org.example.food.*;
import org.example.food_preferences.*;

public class Dolphin extends Predator implements Water {
  @Override
  public void printSwim() {
    System.out.println("Дельфин плывёт");
  }

  @Override
  public void printEat(Food fish) {
    if (fish instanceof Fish) {
      System.out.format("Дельфин ест %s\n", ((Fish) fish).toString());
    } else {
      throw new WrongFoodException("Wrong food choice");
    }
  }
}
