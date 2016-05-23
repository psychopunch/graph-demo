package me.psychopunch.demo;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;

public class Path {

    private List<Edge> edges = new ArrayList<>();

    public Path(Edge... edges) {
        this.edges = asList(edges);
    }

    public Node getStation(int order) {
        List<Node> nodes = new ArrayList<>();
        int index = 1;
        for (Edge edge : edges) {
            nodes.add(edge.getOrigin());
            if (index == edges.size()) nodes.add(edge.getDestination());
            index++;
        }
        return nodes.get(order);
    }

    public int getTotalDistance() {
        int totalDistance = 0;
        for (Edge edge : edges) totalDistance += edge.getDistance();
        return totalDistance;
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        text.append(format("[%s] : ", getTotalDistance()));
        Edge lastEdge = null;
        for (Edge edge : edges) {
            text.append(format("(%s)", edge.getOrigin()));
            text.append(" -> ");
            lastEdge = edge;
        }
        text.append(format("(%s)", lastEdge.getDestination()));
        return text.toString();
    }

    public int countStops() {
        return edges.size();
    }
}
