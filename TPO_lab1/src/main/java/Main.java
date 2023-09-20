import task2.Dijkstra;
import task2.Graph;
import task2.Node;
import task3.Atmosphere;
import task3.Atmosphere.Weather;
import task3.CoordsPair;
import task3.Human;
import task3.Life;

public class Main {

  public static void main(String[] args) {
    Atmosphere atmosphere = new Atmosphere(Weather.SUNNY, 1, 2, false);
    Life life = new Life(atmosphere);
    Human arthur = new Human("Arturitto", 20);
    Human oldman = new Human("Starik", 102);

    life.addCreature(arthur, new CoordsPair(0, 0));
    life.addCreature(oldman, new CoordsPair(1, 1));

    life.moveCreature(new CoordsPair(0,0), new CoordsPair(1, 1));
  }
}
