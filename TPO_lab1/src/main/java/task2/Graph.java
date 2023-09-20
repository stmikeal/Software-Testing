package task2;

import lombok.Getter;

import java.util.*;

public class Graph {

    @Getter
    private final List<Node> nodes = new LinkedList<>();

    public void addNode(Node nodeA) {
        nodes.add(nodeA);
    }

    public void addAll(Set<Node> nodes) {this.nodes.addAll(nodes);}

    public Optional<Node> findNode(String name) {
        for(Node node : nodes) {
            if (node.getName().equals(name)) return Optional.of(node);
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        return nodes.toString();
    }
}