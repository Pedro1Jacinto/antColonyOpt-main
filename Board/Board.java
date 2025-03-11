package Board;

import java.util.List;

public interface Board {

    void addEdge(int start, int destination, float weight); //method that adds an edge to a graph
    void setNumV(int Vertices);     //method that sets the number of vertices in the graph
    int getNumV();          //method that gets the number of vertices in the graph
    void printGraph();      //method that prints the graph
    List<Edge> getNodeEdges(int node);      // method that given a node gets its edges and
    boolean isEdgeExists(int start, int destination);   //method that checks if edge is already in the graph

}
