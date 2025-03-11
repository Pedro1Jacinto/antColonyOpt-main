
package Board;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Implements the graph to be used in the simulation
 */
public class Graph implements Board {
    /**
     * HashMap of nodes and a list of his edges
     */
    protected Map<Integer, ArrayList<Edge>> graph;
    /**
     * Number of vertices in the graph
     */
    protected int numV;
    protected double totalWeight = 0;

    /**
     * Constructor of Graph when given a file that gets scanned and creates
     * the edges in the file
     *
     * @param file
     */
    public Graph(File file) {

        try {
            int node = 0;
            Scanner scanner = new Scanner(file);

            /**
             * Reads first line of file
             */
            if (scanner.hasNextLine()) {
                String firstLine = scanner.nextLine();
                String[] line1 = firstLine.split(" ");

                /**
                 * Gets the number of vertices and creates the hashmap
                 */
                setNumV(Integer.parseInt(line1[0]));
                this.graph = new HashMap<>();
                for (int i = 0; i < numV; i++) {
                    graph.put(i, new ArrayList<>());
                }
            }

            /**
             * Reads the following lines and adds the edges to the graph
             */
            while (scanner.hasNextLine()) {
                String Lines = scanner.nextLine();
                String[] line = Lines.split(" ");
                for (int j = 0; j < numV; j++) {
                    int weight = Integer.parseInt(line[j]);
                    totalWeight += weight;
                    if (weight != 0) addEdge(node, j, weight);
                }
                node++;
            }
        } catch (FileNotFoundException e) {
            System.out.println(("File not found: " + e.getMessage()));
        }
    }

    /**
     * Constructor of Graph when given the number of vertices and the
     * max weight of the edges
     *
     * @param Vertices
     * @param Max
     */
    public Graph(String Vertices, String Max) {
        setNumV(Integer.parseInt(Vertices));
        this.graph = new HashMap<>();
        for (int i = 0; i < numV; i++) {
            graph.put(i, new ArrayList<>());
        }
        /**
         * Creates random edges between the nodes with a random weight
         */
        Random random = new Random();
        float maxW = Float.parseFloat(Max);
        for (int i = 0; i < getNumV(); i++) {
            int numEdges = random.nextInt(2 * getNumV()) + 1;

            for (int j = 0; j < numEdges; j++) {
                int destination = random.nextInt(getNumV());
                float weight;
                /**
                 * gets a random weight to the edge and guarantees its not zero
                 */
                do {
                    weight = random.nextInt((int) maxW);
                } while (weight == 0);
                /**
                 * if the edge already exists or the destination is the node itself
                 * its not added in the graph
                 */
                if (destination != i && !isEdgeExists(i, destination) && !isEdgeExists(destination, i)) {
                    addEdge(i, destination, weight);
                    addEdge(destination, i, weight);
                    totalWeight += weight;
                }
            }
        }
    }

    /**
     * Gets the total weight of the graph
     *
     * @return double
     */
    public double getTotalWeight() {
        return totalWeight;
    }

    /**
     * Method to add an edge to the graph
     *
     * @param start
     * @param destination
     * @param weight
     */
    public void addEdge(int start, int destination, float weight) {

        graph.get(start).add(new Edge(start, destination, weight));
    }

    /**
     * Method to set the number of vertices in the graph
     *
     * @param Vertices
     */
    public void setNumV(int Vertices) {
        this.numV = Vertices;
    }

    /**
     * Method to get the number of vertices in the graph
     *
     * @return numV
     */
    public int getNumV() {
        return this.numV;
    }

    /**
     * Method to print the Graph
     */
    public void printGraph() {
        boolean found = false;
        float weigth = 0;

        System.out.println("\n");

        for (int vertex : graph.keySet()) {
            for (int i = 0; i < numV; i++) {
                for (Edge edge : graph.get(vertex)) {
                    if (edge.getDest() == i) {
                        found = true;
                        weigth = edge.getWeight();
                        break;
                    }
                }

                if (found) {
                    System.out.print(weigth + " ");
                } else System.out.print(0.0 + " ");

                found = false;
            }
            System.out.println();
        }

        System.out.println("\n");
    }

    /**
     * Method to get every edge connected to a given node
     *
     * @param node
     * @return List of edges
     */
    public List<Edge> getNodeEdges(int node) {
        if (graph.containsKey(node)) { //Prof told to suppress warning
            return (ArrayList<Edge>) graph.get(node).clone();
        } else {
            return new ArrayList<>();

        }
    }

    /**
     * Method to see if a given edge already existis in the graph
     *
     * @param start
     * @param destination
     * @return True if exists, False if it doesn't
     */
    public boolean isEdgeExists(int start, int destination) {
        List<Edge> edges = graph.get(start);

        for (Edge edge : edges) {
            if (edge.getDest() == destination) {
                return true;
            }
        }

        return false;
    }

}
