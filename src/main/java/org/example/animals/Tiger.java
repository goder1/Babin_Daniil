package org.example.animals;

import org.example.area.Ground;
import org.example.food.*;
import org.example.food_preferences.*;

public class Tiger extends Predator implements Ground {
  @Override
  public void printGo() {
    System.out.println("Тигр идёт");
  }

  @Override
  public void printEat(Food meat) {
    if (meat instanceof Beef) {
      System.out.format("Тигр ест %s\n", ((Beef) meat).toString());
    } else {
      throw new WrongFoodException("Wrong food choice");
    }
  }
}
