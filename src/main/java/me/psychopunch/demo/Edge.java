package me.psychopunch.demo;

import static java.lang.String.format;

public class Edge {

    private final Node origin;
    private final Node destination;

    private final int distance;

    public Edge(Node origin, Node destination, int distance) {
        this.origin = origin;
        this.destination = destination;
        this.distance = distance;
    }

    public Node getOrigin() {
        return origin;
    }

    public int getDistance() {
        return distance;
    }

    public Node getDestination() {
        return destination;
    }

    public String getDestinationName() {
        return destination.getName();
    }

    @Override
    public String toString() {
        return format("%s -> %s", origin.getName(), destination.getName());
    }
}
