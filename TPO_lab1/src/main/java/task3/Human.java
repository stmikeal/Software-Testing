package task3;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Human {

  private String name;
  private int age;
  private int coordX;
  private int coordY;
  private Set<Human> fieldOfVision = new HashSet<>();
  private int rangeOfVision;

  public Human(String name, int age) {
    this.name = name;
    this.age = age;
  }

  public boolean makeStep(boolean forward, boolean axisX) {
    int newX;
    int newY;
    int step = forward ? 1 : -1;

    newX = axisX ? coordX + step : coordX;
    newY = axisX ? coordY : coordY + step;

    for (Human c : fieldOfVision)
      if (c.coordX == newX && c.coordY == newY)
        return false;

    coordX = newX;
    coordY = newY;

    return true;
  }

  @Override
  public String toString() {
    return "Human with name " + name;
  }

  public void setCoords(int x, int y) {
    this.coordX = x;
    this.coordY = y;
  }
}
