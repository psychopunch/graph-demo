package me.psychopunch.demo;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class GraphTest {

    @Test
    public void testAddEdge() {
        //given:
        Node a = new Node("A");
        Node b = new Node("B");
        Node c = new Node("C");

        //and:
        Graph graph = new Graph();

        //when:
        Edge edgeAB = graph.addEdge(a, b, 10);
        Edge edgeBA = graph.addEdge(b, a, 17);

        //and:
        graph.addEdge(b, c, 2);

        //then:
        assertNotNull(edgeAB);
        assertEquals(10, edgeAB.getDistance());
        assertTrue(graph.hasEdge(a, b));

        //and:
        assertNotNull(edgeBA);
        assertEquals(17, edgeBA.getDistance());
        assertTrue(graph.hasEdge(b, a));

        //and:
        assertFalse(graph.hasEdge(a, c));
        assertTrue(graph.hasEdge(b, c));
    }

    //TODO: add checks for duplicate edges?

    @Test
    public void testGetPath() {
        //given:
        Node a = new Node("A");
        Node b = new Node("B");
        Node c = new Node("C");
        Node d = new Node("D");

        //and:
        Graph graph = new Graph();
        graph.addEdge(a, b, 5);
        graph.addEdge(b, c, 4);

        //when:
        Path existentPath = graph.getPath(a, b, c);
        Path nonExistentPath = graph.getPath(a, b, c, d);

        //then:
        assertNotNull(existentPath);
        assertEquals(a, existentPath.getStation(0));

        //and:
        assertNull(nonExistentPath);
    }

    @Test
    public void testGetPathByNodeNames() {
        //given:
        Node a = new Node("A");
        Node b = new Node("B");
        Node c = new Node("C");

        //and:
        Graph graph = new Graph();
        graph.addEdge(b, a, 9);
        graph.addEdge(c, b, 10);
        graph.addEdge(c, a, 7);

        //when:
        Path path = graph.getPath("C", "B", "A");

        //then:
        assertNotNull(path);
        assertEquals(19, path.getTotalDistance());
    }

    @Test
    public void testFindPathsWithMaximumStops() {
        //given:
        Graph graph = Graph.create("AB5", "BC4", "CD8", "DC8", "DE6", "AD5",
                "CE2", "EB3", "AE7");

        //when:
        List<Path> pathsMax3 = graph.findPathsWithMaximumStops("C", "C", 3);
        List<Path> pathsMax2 = graph.findPathsWithMaximumStops("C", "C", 2);
        List<Path> nonExistent = graph.findPathsWithMaximumStops("E", "A", 10);

        //then:
        assertNotNull(pathsMax3);
        assertEquals(2, pathsMax3.size());
        List<Integer> expectedDistances = asList(16, 9);
        for (Path path :pathsMax3) {
            assertTrue("Total distance should be any of the expected.",
                    expectedDistances.contains(path.getTotalDistance()));
        }

        //and:
        assertNotNull(pathsMax2);
        assertEquals(1, pathsMax2.size());
        Path path = pathsMax2.get(0);
        assertEquals("C", path.getStation(0).getName());
        assertEquals("D", path.getStation(1).getName());
        assertEquals("C", path.getStation(2).getName());

        //and:
        assertNotNull(nonExistent);
        assertTrue(nonExistent.isEmpty());
    }

    @Test
    public void testFindPathsWithMaximumDistance() {
        //given:
        Graph graph = Graph.create("AB5", "BC4", "CD8", "DC8", "DE6", "AD5",
                "CE2", "EB3", "AE7");

        //when:
        List<Path> paths = graph.findPathsWithMaximumDistance("C", "C", 30);

        //then:
        assertNotNull(paths);
        assertEquals(7, paths.size());

        //and:
        for (Path path : paths) {
            assertTrue(path.getTotalDistance() < 30);
            assertEquals("C", path.getStation(0).getName());
            assertEquals("C", path.getStation(path.countStops()).getName());
        }
    }

    @Test
    public void testFindShortestPath() {
        //given:
        Graph graph = Graph.create("AB5", "BC4", "CD8", "DC8", "DE6", "AD5",
                "CE2", "EB3", "AE7");

        //when:
        Path pathAC = graph.findShortestPath("A", "C");
        Path pathBB = graph.findShortestPath("B", "B");
        Path nonExistent = graph.findShortestPath("D", "A");

        //then:
        assertNotNull(pathAC);
        assertEquals(9, pathAC.getTotalDistance());

        //and:
        assertNotNull(pathBB);
        assertEquals(9, pathBB.getTotalDistance());

        //and:
        assertNull(nonExistent);
    }

    @Test
    public void testCreate() {
        //given:
        Graph graph = Graph.create("AB5", "BC4", "CD8", "DC8", "DE6");

        //expect:
        assertNotNull(graph);

        //and:
        assertTrue(graph.hasEdge("A", "B"));
        assertTrue(graph.hasEdge("B", "C"));
        assertTrue(graph.hasEdge("C", "D"));
        assertTrue(graph.hasEdge("D", "C"));
        assertTrue(graph.hasEdge("D", "E"));

        //and:
        assertEquals(5, graph.findEdge("A", "B").getDistance());
        assertEquals(4, graph.findEdge("B", "C").getDistance());
        assertEquals(8, graph.findEdge("C", "D").getDistance());
        assertEquals(8, graph.findEdge("D", "C").getDistance());
        assertEquals(6, graph.findEdge("D", "E").getDistance());

        //and:
        assertFalse(graph.hasEdge("D", "A"));
        assertFalse(graph.hasEdge("E", "B"));
    }

    @Test
    public void testCreateWithVaryingCase() {
        //given:
        Graph graph = Graph.create("aB9", "bc10", "CD2");

        //expect:
        assertTrue(graph.hasEdge("A", "b"));
        assertTrue(graph.hasEdge("B", "C"));
        assertTrue(graph.hasEdge("c", "d"));
    }

    //TODO update create method to check for correct inputs

    @Test
    public void testFindEdge() {
        //given:
        Node a = new Node("A");
        Node b = new Node("B");
        Node c = new Node("C");

        //and:
        Graph graph = new Graph();
        graph.addEdge(a, b, 2);
        graph.addEdge(b, c, 4);
        graph.addEdge(c, b, 5);

        //and:
        Edge edgeBA = graph.addEdge(b, a, 7);

        //expect:
        assertEquals(edgeBA, graph.findEdge("B", "A"));
        assertNull(graph.findEdge("A", "C"));
    }

}
