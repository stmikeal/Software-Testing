package task3;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class Life {

    private final Map<CoordsPair, Human> creatureMap;
    private final Atmosphere atmosphere;

    public Life(Atmosphere atmosphere) {
        this.atmosphere = atmosphere;
        this.creatureMap = new HashMap<>();
    }

    public boolean addCreature(Human human, CoordsPair coords) {
        if (creatureMap.get(coords) != null) {
            return false;
        }
        int visibility = 2;
        if (!atmosphere.isMoonVisible()) {
            visibility -= 1;
        }
        if (atmosphere.isRarefied()) {
            visibility -= 1;
        }

        human.setRangeOfVision(visibility);
        human.setCoordX(coords.getKey());
        human.setCoordY(coords.getValue());
        creatureMap.put(coords, human);
        updateFOV(human);
        return true;
    }

    public boolean moveCreature(CoordsPair oldCoords, CoordsPair newCoords) {
        if (creatureMap.get(oldCoords) == null) {
            return false;
        }
        Human human = creatureMap.get(oldCoords);
        boolean forward = newCoords.getKey() > oldCoords.getKey();
        int xDist = Math.abs(newCoords.getKey() - oldCoords.getKey());
        int yDist = Math.abs(newCoords.getValue() - oldCoords.getValue());

        long count = human.getFieldOfVision().stream().filter(
                stranger -> stranger.getCoordX() == newCoords.getKey() && stranger.getCoordY() == newCoords.getValue()
        ).count();
        if (count != 0) return false;

        for (int i = 0; i < xDist; i++) {
            if (!human.makeStep(forward, true)) {
                updateCoords(oldCoords);
                return false;
            }
            Human stranger = checkForCollision(human);
            if (stranger != null) {
                human.getFieldOfVision().add(stranger);
                stranger.getFieldOfVision().add(human);
                human.makeStep(!forward, true);
                return false;
            }
            updateCoords(oldCoords);
            oldCoords = new CoordsPair(human.getCoordX(), human.getCoordY());
            updateFOV(human);

        }

        forward = newCoords.getValue() > oldCoords.getValue();

        for (int i = 0; i < yDist; i++) {
            if (!human.makeStep(forward, false)) {
                updateCoords(oldCoords);
                return false;
            }
            Human stranger = checkForCollision(human);
            if (stranger != null) {
                human.getFieldOfVision().add(stranger);
                stranger.getFieldOfVision().add(human);
                human.makeStep(!forward, false);
                return false;
            }
            updateCoords(oldCoords);
            oldCoords = new CoordsPair(human.getCoordX(), human.getCoordY());
            updateFOV(human);
        }
        return true;
    }

    protected void updateFOV(Human human) {
        Set<Human> visibleHumans = creatureMap.entrySet().stream().filter(entry ->
                Math.abs(entry.getKey().getKey() - human.getCoordX()) <= human.getRangeOfVision() &&
                        Math.abs(entry.getKey().getValue() - human.getCoordY()) <= human.getRangeOfVision() &&
                        (entry.getKey().getValue() != human.getCoordY() ||
                                entry.getKey().getKey() != human.getCoordX())
        ).map(Map.Entry::getValue).collect(Collectors.toSet());
        human.getFieldOfVision().forEach(stranger -> stranger.getFieldOfVision().remove(human));
        human.setFieldOfVision(visibleHumans);
        visibleHumans.forEach(stranger -> stranger.getFieldOfVision().add(human));
    }

    protected void updateCoords(CoordsPair oldCoords) {
        Human human = creatureMap.get(oldCoords);
        CoordsPair newCoords = new CoordsPair(human.getCoordX(), human.getCoordY());
        creatureMap.remove(oldCoords);
        creatureMap.put(newCoords, human);
    }

    protected Human checkForCollision(Human human) {
        if (creatureMap.containsKey(new CoordsPair(human.getCoordX(), human.getCoordY()))
                && creatureMap.get(new CoordsPair(human.getCoordX(), human.getCoordY())) != null) {
            System.out.println(human.getName() + " столкнулся с " + creatureMap.get(
                    new CoordsPair(human.getCoordX(), human.getCoordY())).getName() + "!");
            return creatureMap.get(new CoordsPair(human.getCoordX(), human.getCoordY()));
        }
        return null;
    }
}
