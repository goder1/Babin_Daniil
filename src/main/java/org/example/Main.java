package org.example;

import org.example.food.*;
import org.example.animals.*;

public class Main {
  public static void main(String[] args) {
    Horse horse = new Horse();
    Tiger tiger = new Tiger();
    Camel camel = new Camel();
    Eagle eagle = new Eagle();
    Dolphin dolphin = new Dolphin();

    Meat meat = new Meat();
    Beef beef = new Beef();
    Fish fish = new Fish();
    Grass grass = new Grass();

    horse.printEat(grass);
    horse.printGo();

    tiger.printEat(beef);
    tiger.printGo();

    camel.printEat(grass);
    camel.printGo();

    dolphin.printEat(fish);
    dolphin.printSwim();

    eagle.printFly();
    eagle.printEat(grass);
  }
}