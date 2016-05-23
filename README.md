# Graph

## Discussion
The graph data structure is at the heart of the solution for the code assignment. Problems that required searches for multiple Paths employ depth first search with the aid of a stack for traversal. In this solution, domain driven design principles have been applied. Graph data structure related idioms such as node, edge, etc. are mapped to respective recognizable terms within the (assumed) ubiquitous language. The graph data structure itself is represented as the `Graph` class. Classes such as `Node`, `Edge` etc. are implemented as value object classes that are (mostly) immutable.

There are a few verification, and input checks in place, but there are still some others that have not been implemented yet. In particular, this application assumes that input is well formatted. At the time of writing, there have been no tests done to check its behavior when given an input file using an invalid format.

## User Guide

### Gradle
This application was built (and tested) using Gradle 2.9. Since no executables are allowed for submission, this code base does not contain any Gradle wrapper. To run this application, an installation of Gradle on the local system is required. Please consult the [official installation documentation](https://docs.gradle.org/current/userguide/installation.html) for instructions on how to set up Gradle.

### Execution
With Gradle successfully set up, the application can be executed using the following command:

    gradle run -Dgraph.file=graph.dat -q

where `graph.dat` is the input file that describes the Graph. The `-q` option hides the build information texts displayed normally by Gradle. This project contains a sample graph file named `graph.dat` in root directory.