package task2;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class Node{

  private String name;
  private List<Node> shortestPath = new LinkedList<>();
  private Integer distance = Integer.MAX_VALUE;
  Map<Node, Integer> adjacentNodes = new HashMap<>();

  public void addDestination(Node destination, int distance) {
    adjacentNodes.put(destination, distance);
    destination.adjacentNodes.put(this, distance);
  }
  public Node(String name) {
    this.name = name;
  }
  public Node(String name, Integer distance) {
    this.distance = distance;
    this.name = name;
  }

  @Override
  public String toString() {
    return name + " : " + distance;
  }

  @Override
  public boolean equals(Object obj) {
    return obj.getClass() == this.getClass() && Objects.equals(((Node) obj).getDistance(), this.getDistance());
  }
}
