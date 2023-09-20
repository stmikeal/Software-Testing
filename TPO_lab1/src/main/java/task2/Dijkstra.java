package task2;

import lombok.Getter;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Getter
public class Dijkstra {

  Set<Node> settledNodes = new HashSet<>();
  Set<Node> unsettledNodes = new HashSet<>();

  public void calculateShortestPathFromSource(Node source) {
    if (source == null) return;

    source.setDistance(0);
    unsettledNodes.add(source);
    Node currentNode = getLowestDistanceNode();

    while (unsettledNodes.size() != 0) {
      currentNode = iteration(currentNode);
    }
  }

  protected Node iteration(Node currentNode) {
    unsettledNodes.remove(currentNode);

    currentNode
            .getAdjacentNodes()
            .entrySet()
            .stream().filter(entry -> !settledNodes.contains(entry.getKey()))
            .forEach(entry -> {
              calculateMinimumDistance(entry.getKey(), entry.getValue(), currentNode);
              unsettledNodes.add(entry.getKey());
            });

    settledNodes.add(currentNode);
    return getLowestDistanceNode();
  }

  protected Node getLowestDistanceNode() {
    return unsettledNodes.stream().min(Comparator.comparing(Node::getDistance)).orElse(null);
  }

  protected void calculateMinimumDistance(Node evaluationNode, Integer edgeWeigh, Node sourceNode) {
    Integer sourceDistance = sourceNode.getDistance();
    if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
      evaluationNode.setDistance(sourceDistance + edgeWeigh);
      LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
      shortestPath.add(sourceNode);
      evaluationNode.setShortestPath(shortestPath);
    }
  }
}
