package task3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ArturoLifeTest {
    private static Atmosphere atmosphere;
    private static Life life;
    private static Human arthur;
    private static Human oldman;

    @BeforeAll
    static void init() {
        arthur = new Human("Arturitto", 20);
        oldman = new Human("Starik", 102);
    }

    @BeforeEach
    public void goBack() {
        atmosphere = new Atmosphere(Atmosphere.Weather.SUNNY, 2, 2, false);
        life = new Life(atmosphere);
        life.addCreature(arthur, new CoordsPair(0, 0));
        life.addCreature(oldman, new CoordsPair(1, 1));
    }

    @Test
    public void coordTest() {
        CoordsPair coord = new CoordsPair(1, 1);
        Assertions.assertEquals("X: 1, Y: 1", coord.toString());
        coord.setKey(2);
        coord.setValue(3);
        Assertions.assertEquals("X: 2, Y: 3", coord.toString());
    }

    @Test
    public void atmosphereTest() {
        Assertions.assertEquals("Typical Atmosphere. Humidity: 2,000000. Density: 2,000000.", atmosphere.toString());
        atmosphere.setDensity(1);
        Assertions.assertTrue(atmosphere.isRarefied());
        atmosphere.setDensity(-1);
        Assertions.assertEquals("Atmosphere of different universe", atmosphere.toString());
    }

    @Test
    public void humanTest() {
        Assertions.assertTrue(arthur.makeStep(true, true));
        Assertions.assertFalse(arthur.makeStep(true, false));
        Assertions.assertEquals(1, arthur.getCoordX());
        Assertions.assertEquals(0, arthur.getCoordY());
        Assertions.assertEquals("Human with name Arturitto", arthur.toString());
        Assertions.assertTrue(arthur.makeStep(false, true));
    }

    @Test
    public void movementTest() {
        Assertions.assertEquals(2, life.getCreatureMap().size());
        Assertions.assertFalse(life.moveCreature(new CoordsPair(0, 0), new CoordsPair(1, 1)));
        Assertions.assertEquals(0, arthur.getCoordX());
        Assertions.assertEquals(0, arthur.getCoordY());
        atmosphere.setDensity(1);
        life = new Life(atmosphere);
        life.addCreature(arthur, new CoordsPair(0, 0));
        life.addCreature(oldman, new CoordsPair(1, 1));
        Assertions.assertEquals(0, arthur.getFieldOfVision().size());
        Assertions.assertFalse(life.moveCreature(new CoordsPair(0, 0), new CoordsPair(1, 1)));
        Assertions.assertEquals(1, arthur.getCoordX());
        Assertions.assertEquals(0, arthur.getCoordY());
        Assertions.assertEquals(1, arthur.getFieldOfVision().size());
        Assertions.assertEquals(1, oldman.getFieldOfVision().size());
        Assertions.assertFalse(life.moveCreature(new CoordsPair(1, 1), new CoordsPair(1, 0)));
        Assertions.assertEquals(1, oldman.getCoordX());
        Assertions.assertEquals(1, oldman.getCoordY());
    }

    @Test
    public void fieldOfVisionTest() {
        atmosphere.setMoonVisible(true);
        life = new Life(atmosphere);
        life.addCreature(arthur, new CoordsPair(0, 0));
        life.addCreature(oldman, new CoordsPair(3, 0));
        life.addCreature(new Human("Mike", 43), new CoordsPair(3, 1));
        life.addCreature(new Human("Roman", 28), new CoordsPair(3, 2));
        life.addCreature(new Human("Not Mike", 34), new CoordsPair(3, -1));
        life.addCreature(new Human("Not Roman", 82), new CoordsPair(3, -2));
        Assertions.assertEquals(0, arthur.getFieldOfVision().size());
        Assertions.assertEquals(4, oldman.getFieldOfVision().size());
        Assertions.assertTrue(life.moveCreature(new CoordsPair(0, 0), new CoordsPair(1, 0)));
        Assertions.assertEquals(5, arthur.getFieldOfVision().size());
        Assertions.assertEquals(5, oldman.getFieldOfVision().size());
        Assertions.assertTrue(life.moveCreature(new CoordsPair(1, 0), new CoordsPair(-3, -3)));
        Assertions.assertEquals(0, arthur.getFieldOfVision().size());
        Assertions.assertEquals(4, oldman.getFieldOfVision().size());
        Assertions.assertEquals(-3, arthur.getCoordX());
        Assertions.assertEquals(-3, arthur.getCoordY());
    }

    @Test
    public void notUniqueCoordCreatureTest() {
        Assertions.assertFalse(life.addCreature(arthur, new CoordsPair(0, 0)));
        Assertions.assertTrue(life.addCreature(arthur, new CoordsPair(42, 42)));
    }
}
