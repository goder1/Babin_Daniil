package org.example.animals;

import org.example.area.Ground;
import org.example.food_preferences.*;
import org.example.food.*;


public class Camel extends Herbivorous implements Ground {
  @Override
  public void printGo() {
    System.out.println("Верблюд идёт");
  }

  @Override
  public void printEat(Food grass) {
    if (grass instanceof Grass) {
      System.out.format("Верблюд ест %s\n", ((Grass) grass).toString());
    } else {
      throw new WrongFoodException("Wrong food choice");
    }
  }
}
