package org.example.animals;

import org.example.area.Ground;
import org.example.food.*;
import org.example.food_preferences.*;

public class Horse extends Herbivorous implements Ground {
  @Override
  public void printGo() {
    System.out.println("Лошадь идёт");
  }

  @Override
  public void printEat(Food grass) {
    if (grass instanceof Grass) {
      System.out.format("Лошадь ест %s\n", ((Grass) grass).toString());
    } else {
      throw new WrongFoodException("Wrong food choice");
    }
  }
}
