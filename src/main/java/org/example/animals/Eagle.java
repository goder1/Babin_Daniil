package org.example.animals;

import org.example.area.Air;
import org.example.food.*;
import org.example.food_preferences.*;

public class Eagle extends Predator implements Air {
  @Override
  public void printFly() {
    System.out.println("Орёл летит");
  }

  @Override
  public void printEat(Food meat) {
    if (meat instanceof Meat) {
      System.out.format("Орёл ест %s\n", ((Meat) meat).toString());
    } else {
      throw new WrongFoodException("Wrong food choice");
    }
  }
}
