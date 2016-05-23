package me.psychopunch.demo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;

public class GraphApplication {

    public static void main(String[] args) {
        Graph graph = processGraph();

        String[][] paths = {{"A", "B", "C"}, {"A", "D"}, {"A", "D", "C"}, {"A", "E", "B", "C", "D"},
                {"A", "E", "D"}};
        for (int index = 0; index < paths.length; index++) {
            Path path = graph.getPath(paths[index]);
            printPathAsOutput(index + 1, paths[index], path);
        }

        List<Path> paths3Max = graph.findPathsWithMaximumStops("C", "C", 3);
        System.out.println(format("Item  6: Path C -> C, 3 stops max > %s", paths3Max.size()));

        List<Path> paths4Max = graph.findPathsWithMaximumStops("A", "C", 4);
        List<Path> paths4Exactly = new ArrayList<>();
        for (Path path : paths4Max) {
            if (4 == path.countStops()) paths4Exactly.add(path);
        }
        System.out.println(format("Item  7: Path A -> C, 4 stops exactly > %s", paths4Exactly.size()));

        Path shortestPathAC = graph.findShortestPath("A", "C");
        System.out.println(format("Item  8: A -> C, shortest path > %s", shortestPathAC.getTotalDistance()));

        Path shortestPathBB = graph.findShortestPath("B", "B");
        System.out.println(format("Item  9: B -> B, shortest path > %s", shortestPathBB.getTotalDistance()));

        List<Path> pathsUpperBoundDistance30 = graph.findPathsWithMaximumDistance("C", "C", 30);
        System.out.println(format("Item 10: C -> C, max distance 30 > %s", pathsUpperBoundDistance30.size()));
    }

    private static Graph processGraph() {
        String fileName = System.getProperty("graph.file");
        if (fileName == null) {
            System.out.println("This application expects 'graph.file' property.");
            System.exit(1);
        }

        Graph graph = null;
        File inputFile = new File(fileName);
        try {
            FileReader fileReader = new FileReader(inputFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String input = bufferedReader.readLine();
            System.out.println(format("Input: %s", input));

            String[] edgeInputs = input.split(", ");
            graph = Graph.create(edgeInputs);
        } catch (FileNotFoundException e) {
            System.out.println(format("The file specified by path [%s] was not found.", fileName));
            System.exit(1);
        } catch (IOException e) {
            System.out.print("An error occured while reading input file.");
            e.printStackTrace();
            System.exit(1);
        }
        return graph;
    }

    private static void printPathAsOutput(int count, String[] pathInput, Path path) {
        String output = "NO SUCH PATH";
        List<String> inputs = asList(pathInput);
        String format = "Item %2d: %-17s> %-20s";
        if (path != null) {
            output = path.toString();
        }
        System.out.println(format(format, count, inputs, output));
    }

}
