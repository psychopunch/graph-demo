package me.psychopunch.demo;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PathTest {

    @Test
    public void testGetTotalDistance() {
        //given:
        Node a = new Node("A");
        Node b = new Node("B");
        Node c = new Node("C");
        Node d = new Node("D");

        //and:
        Edge edgeAB = new Edge(a, b, 10);
        Edge edgeBC = new Edge(b, c, 7);
        Edge edgeCD = new Edge(c, d, 5);

        //and:
        Path pathABC = new Path(edgeAB, edgeBC);
        Path pathABCD = new Path(edgeAB, edgeBC, edgeCD);

        //expect:
        assertEquals(17, pathABC.getTotalDistance());
        assertEquals(22, pathABCD.getTotalDistance());
    }

}
