package task2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;

public class DijkstraTest {
    private Graph graph;
    private Dijkstra dijkstra;
    private static Graph expected;
    private final List<List<Node>> checkUnsettled = Arrays.asList(
            List.of(new Node("A", 0)),
            List.of(new Node("C", 15), new Node("B", 10)),
            List.of(new Node("C", 15), new Node("D", 22), new Node("F", 25)),
            List.of(new Node("D", 22), new Node("F", 25), new Node("E", 25)),
            List.of(new Node("F", 23), new Node("E", 24)),
            List.of(new Node("E", 24))
    );

    @BeforeAll
    static void init() {
        expected = new Graph();
        expected.addAll(Set.of(
                new Node("A", 0),
                new Node("B", 10),
                new Node("C", 15),
                new Node("D", 22),
                new Node("E", 24),
                new Node("F", 23)
        ));
    }

    @BeforeEach
    public void initGraph() {
        graph = new Graph();
        dijkstra = new Dijkstra();
        Node nodeA = new Node("A");
        Node nodeB = new Node("B");
        Node nodeC = new Node("C");
        Node nodeD = new Node("D");
        Node nodeE = new Node("E");
        Node nodeF = new Node("F");

        nodeA.addDestination(nodeB, 10);
        nodeA.addDestination(nodeC, 15);
        nodeB.addDestination(nodeD, 12);
        nodeB.addDestination(nodeF, 15);
        nodeC.addDestination(nodeE, 10);
        nodeD.addDestination(nodeE, 2);
        nodeD.addDestination(nodeF, 1);
        nodeF.addDestination(nodeE, 5);

        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);
        graph.addNode(nodeE);
        graph.addNode(nodeF);
    }

    @Test
    public void testOnFinalDistanceDependsA() {
        dijkstra.calculateShortestPathFromSource(graph.findNode("A").orElse(null));
        graph.getNodes().forEach(node ->
                    Assertions.assertTrue(
                            node.equals(Objects.requireNonNull(expected.findNode(node.getName()).orElse(null)))
                    )
        );
    }

    @Test
    public void testOnIteration() {
        Node source = graph.findNode("A").orElse(null);
        Assertions.assertNotNull(source);

        source.setDistance(0);
        dijkstra.getUnsettledNodes().add(source);
        Node currentNode = dijkstra.getLowestDistanceNode();

        int counter = 0;
        while (dijkstra.unsettledNodes.size() != 0) {
            final List<Node> unsettled = checkUnsettled.get(counter);
            dijkstra.getUnsettledNodes().forEach(node -> {
                Assertions.assertTrue(unsettled.contains(node));
            });
            Assertions.assertEquals(unsettled.size(), dijkstra.getUnsettledNodes().size());
            currentNode = dijkstra.iteration(currentNode);
            Assertions.assertTrue(expected.getNodes().containsAll(dijkstra.getSettledNodes()));
            Assertions.assertEquals(dijkstra.settledNodes.size(), ++counter);
        }
    }

    @Test
    public void testEmpty() {
        graph = new Graph();
        Node nodeA = new Node("A");
        dijkstra.calculateShortestPathFromSource(nodeA);
        Assertions.assertEquals(0, graph.getNodes().size());
    }

    @Test
    public void testZero() {
        graph = new Graph();
        Node nodeA = new Node("A");
        Node nodeB = new Node("B");
        Node nodeC = new Node("C");

        nodeA.addDestination(nodeB, 0);
        nodeA.addDestination(nodeC, 0);
        nodeB.addDestination(nodeC, 0);

        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);

        dijkstra.calculateShortestPathFromSource(nodeA);
        Assertions.assertEquals(3, graph.getNodes().size());
        graph.getNodes().forEach(node -> {
            Assertions.assertEquals(node.getName().equals("A") ? 0 : 1, node.getShortestPath().size());
            Assertions.assertEquals(0, node.getDistance());
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {42, 311, 765, 13, 14, 222})
    public void randomTest(int seed) {
        int maxPathLength = 10;
        int maxEdgeWeight = 200;
        int minEdgeWeight = 100;
        int maxShortEdge = 20;
        int minShortEdge = 10;

        Random random = new Random();
        random.setSeed(seed);
        graph = new Graph();
        Node node = new Node("root");
        graph.addNode(node);

        int pathLength = random.nextInt(maxPathLength);
        int[] piecesOfPath = new int[pathLength];

        for(int i = 0; i < pathLength; i++) {
            Node newNode = new Node(Integer.toString(i));
            int path = minEdgeWeight + random.nextInt(maxEdgeWeight - minEdgeWeight);
            node.addDestination(newNode, path);
            Node shortNode = node;
            int currentPath = 0;
            while(true) {
                int shortPath = minShortEdge + random.nextInt(maxShortEdge - minShortEdge);
                if (currentPath + shortPath >= path) break;
                currentPath += shortPath;
                Node newShortNode = new Node(Integer.toString(i) + "." + Integer.toString(currentPath));
                shortNode.addDestination(newShortNode, shortPath);
                shortNode = newShortNode;
                graph.addNode(shortNode);
            }
            piecesOfPath[i] = currentPath;
            shortNode.addDestination(newNode, 0);
            node = newNode;
            graph.addNode(node);
        }

        dijkstra.calculateShortestPathFromSource(graph.findNode("root").orElse(null));
        System.out.println(graph);

        int path = 0;
        for(int i = 0; i < pathLength; i++) {
            node = graph.findNode(Integer.toString(i)).orElse(null);
            assert node != null;
            Assertions.assertEquals(piecesOfPath[i] + path, node.getDistance());
            path += piecesOfPath[i];
        }
    }

    @Test
    public void fullGraph() {
        graph = new Graph();

        Node nodeA = new Node("A");
        Node nodeB = new Node("B");
        Node nodeC = new Node("C");
        Node nodeD = new Node("D");

        nodeA.addDestination(nodeB, 20);
        nodeA.addDestination(nodeC, 5);
        nodeA.addDestination(nodeD, 30);
        nodeB.addDestination(nodeC, 3);
        nodeB.addDestination(nodeD, 2);
        nodeC.addDestination(nodeD, 1);

        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);

        dijkstra.calculateShortestPathFromSource(nodeA);

        Assertions.assertEquals(0, nodeA.getDistance());
        Assertions.assertEquals(8, nodeB.getDistance());
        Assertions.assertEquals(5, nodeC.getDistance());
        Assertions.assertEquals(6, nodeD.getDistance());
    }
}
