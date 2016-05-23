package me.psychopunch.demo;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;

public class Graph {

    //TODO: restrict creation of Graph to just the create method so that nodeMap
    //this is so that any Graph object has nodeMap correctly built as expected
    private Map<String, Node> nodeMap = new HashMap<>();

    private Map<Node, List<Edge>> graphMap = new HashMap<>();

    public static Graph create(String... inputEdges) {
        Graph graph = new Graph();
        Pattern pattern = Pattern.compile("(\\p{Alpha})(\\p{Alpha})(\\p{Digit}+)");
        for (String input : inputEdges) {
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                //TODO: move the requirement for upper case names to Node class itself
                String originName = matcher.group(1).toUpperCase();
                Node origin = graph.nodeMap.get(originName);
                if (origin == null) {
                    origin = new Node(originName);
                    graph.nodeMap.put(originName, origin);
                }

                String destinationName = matcher.group(2).toUpperCase();
                Node destination = graph.nodeMap.get(destinationName);
                if (destination == null) {
                    destination = new Node(destinationName);
                    graph.nodeMap.put(destinationName, destination);
                }

                String distance = matcher.group(3);
                graph.addEdge(origin, destination, parseInt(distance));
            }
        }
        return graph;
    }

    public Edge addEdge(Node origin, Node destination, int distance) {
        Edge edge = new Edge(origin, destination, distance);
        List<Edge> edges = graphMap.get(origin);
        if (edges == null) {
            edges = new ArrayList<>();
            graphMap.put(origin, edges);
        }
        edges.add(edge);
        List<Edge> destinationEdges = graphMap.get(destination);
        if (destinationEdges == null) graphMap.put(destination, new ArrayList<Edge>());
        return edge;
    }

    public Path getPath(Node... nodes) {
        List<Edge> edges = new ArrayList<>();
        int index = 0;
        for (Node node : nodes) {
            if (index < nodes.length - 1) {
                Edge edge = findEdge(node, nodes[index + 1]);
                if (edge != null) edges.add(edge);
                else break;
            } else {
                break;
            }
            index++;
        }
        return (index == nodes.length - 1) ? new Path(edges.toArray(new Edge[0])) : null;
    }

    public Path getPath(String... nodeNames) {
        List<String> names = asList(nodeNames);
        //TODO: reuse nodeMap field here (once constructor is restricted to creator method)
        Map<String, Node> nodeMap = new HashMap<>();
        for (Node node : graphMap.keySet()) {
            if (names.contains(node.getName())) nodeMap.put(node.getName(), node);
        }

        Node[] nodes = new Node[nodeNames.length];
        for (int index = 0; index < nodeNames.length; index++) {
            nodes[index] = nodeMap.get(nodeNames[index]);
        }
        return getPath(nodes);
    }

    private Edge findEdge(Node origin, Node destination) {
        Edge edge = null;
        List<Edge> edges = graphMap.get(origin);
        for (Edge candidateEdge : edges) {
            if (candidateEdge.getDestination().equals(destination)) edge = candidateEdge;
            if (edge != null) break;
        }
        return edge;
    }

    public Edge findEdge(String originName, String destinationName) {
        Node[] nodes = findOriginAndDestination(originName, destinationName);
        Node origin = nodes[0];
        Node destination = nodes[1];
        return findEdge(origin, destination);
    }

    public boolean hasEdge(Node origin, Node destination) {
        return findEdge(origin, destination) != null;
    }

    public boolean hasEdge(String originName, String destinationName) {
        return findEdge(originName, destinationName) != null;
    }

    public List<Path> findPathsWithMaximumStops(String originName, String destinationName, int maximumStops) {
        List<Path> paths = new ArrayList<>();

        Node[] nodes = findOriginAndDestination(originName, destinationName);
        Node origin = nodes[0];
        Node destination = nodes[1];

        Stack<Edge> stack = new Stack<>();
        Stack<Integer> branching = new Stack<>();
        List<Edge> pathEdges = new ArrayList<>();
        Node currentNode = origin;
        for (Edge edge : graphMap.get(currentNode)) stack.push(edge);
        do {
            Edge currentEdge = stack.pop();
            if (pathEdges.size() == maximumStops) {
                while (!branching.isEmpty()) {
                    int currentBranch = branching.pop() - 1;
                    if (currentBranch < 1 && !pathEdges.isEmpty()) {
                        pathEdges.remove(pathEdges.size() - 1);
                    } else if (currentBranch >= 1) {
                        branching.push(currentBranch);
                        break;
                    }
                }
            } else {
                currentNode = currentEdge.getDestination();
                List<Edge> edges = graphMap.get(currentNode);
                for (Edge edge : edges) stack.push(edge);
                branching.push(edges.size());
                pathEdges.add(currentEdge);
                if (currentEdge.getDestination().equals(destination)) {
                    paths.add(new Path(pathEdges.toArray(new Edge[0])));
                }
            }
        } while (!stack.isEmpty());

        return paths;
    }

    public List<Path> findPathsWithMaximumDistance(String originName, String destinationName, int maxDistance) {
        ArrayList<Path> paths = new ArrayList<>();

        Node[] endPoints = findOriginAndDestination(originName, destinationName);
        Node origin = endPoints[0];
        Node destination = endPoints[1];

        Stack<Edge> stack = new Stack<>();
        Stack<Integer> backtracker = new Stack<>();

        for (Edge edge : graphMap.get(origin)) stack.push(edge);
        List<Edge> pathEdges = new ArrayList<>();
        Node nextNode = null;
        int totalDistance = 0;
        do {
            Edge currentEdge = stack.pop();
            if (totalDistance < maxDistance) {
                pathEdges.add(currentEdge);
                totalDistance += currentEdge.getDistance();

                nextNode = currentEdge.getDestination();
                List<Edge> edges = graphMap.get(nextNode);
                for (Edge edge : edges) stack.push(edge);
                backtracker.push(edges.size());

                if (currentEdge.getDestination().equals(destination)) {
                    if (totalDistance < maxDistance) paths.add(new Path(pathEdges.toArray(new Edge[0])));
                }
            } else {
                int branching = 0;
                while (!backtracker.isEmpty()) {
                    branching = backtracker.pop() - 1;
                    if (branching < 1) {
                        Edge removedEdge = pathEdges.remove(pathEdges.size() - 1);
                        totalDistance -= removedEdge.getDistance();
                    } else {
                        backtracker.push(branching);
                        break;
                    }
                }
            }
        } while (!stack.isEmpty());

        return paths;
    }

    private List<Path> findDirectPaths(String originName, String destinationName) {
        List<Path> directPaths = new ArrayList<>();
        Node[] nodes = findOriginAndDestination(originName, destinationName);
        Node origin = nodes[0];
        Node destination = nodes[1];

        Stack<Edge> stack = new Stack<>();
        Stack<Integer> backtracker = new Stack<>();
        for (Edge edge : graphMap.get(origin)) stack.push(edge);
        List<Edge> pathEdges = new ArrayList<>();
        Set<Node> visitedNodes = new HashSet<>();
        Node nextNode = null;
        do {
            Edge currentEdge = stack.pop();
            if (currentEdge.getDestination().equals(destination)) {
                pathEdges.add(currentEdge);
                backtracker.push(0);
                directPaths.add(new Path(pathEdges.toArray(new Edge[0])));

                int branching = 0;
                while (!backtracker.isEmpty()) {
                    branching = backtracker.pop() - 1;
                    if (branching < 1) {
                        Edge removedEdge = pathEdges.remove(pathEdges.size() - 1);
                        visitedNodes.remove(removedEdge.getDestination());
                    } else {
                        backtracker.push(branching);
                        break;
                    }
                }
            } else {
                pathEdges.add(currentEdge);
                nextNode = currentEdge.getDestination();
                visitedNodes.add(nextNode);

                int branching = 0;
                for (Edge edge : graphMap.get(nextNode)) {
                    if (!visitedNodes.contains(edge.getDestination())) {
                        stack.push(edge);
                        branching++;
                    }
                }
                if (branching > 0) backtracker.push(branching);
            }
        } while (!stack.isEmpty());

        return directPaths;
    }

    public Path findShortestPath(String originName, String destinationName) {
        List<Path> directPaths = findDirectPaths(originName, destinationName);
        Path shortestPath = null;
        if (!directPaths.isEmpty()) shortestPath = directPaths.get(0);
        for (int index = 1; index < directPaths.size(); index++) {
            Path currentPath = directPaths.get(index);
            if (currentPath.getTotalDistance() < shortestPath.getTotalDistance()) {
                shortestPath = currentPath;
            }
        }
        return shortestPath;
    }

    private Node[] findOriginAndDestination(String originName, String destinationName) {
        Node origin = null;
        Node destination = null;
        //TODO: reuse nodeMap field once retriction on constructor is applied
        for (Node node : graphMap.keySet()) {
            if (node.getName().equalsIgnoreCase(originName)) origin = node;
            if (node.getName().equalsIgnoreCase(destinationName)) destination = node;
            if (origin != null && destination != null) break;
        }
        return new Node[] {origin, destination};
    }

}
