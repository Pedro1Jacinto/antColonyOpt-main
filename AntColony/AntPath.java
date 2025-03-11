package AntColony;

import Board.Edge;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the path that each Ant is on
 */
public class AntPath {
    /**
     * List of edges that the Ant has already visited
     */
    private List<Edge> edges;

    /**
     * Constructor of the AntPath that creates the List of edges
     */
    public AntPath() {

        edges = new ArrayList<Edge>(1);

    }

    /**
     * Puts the edge visited in the list
     *
     * @param path
     */
    public void setEdges(List<Edge> path) {
        this.edges = path;
    }

    /**
     * Method that returns the edges on the AntPath
     *
     * @return
     */
    public List<Edge> getEdges() {
        return this.edges;
    }

    public float sumWeight() {
        float total = 0;
        for (Edge edge : edges) {
            total += edge.getWeight();
        }
        return total;
    }

    /**
     * Method that appends the next visited edge
     *
     * @param next
     */
    public void appendPath(Edge next) {

        edges.add(next);
    }

    public void remove(Edge e) {
        edges.remove(e);
    }

    @Override
    public AntPath clone() {
        AntPath a = new AntPath();
        for (Edge aux : this.edges) {
            a.appendPath(aux);
        }
        return a;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        StringBuilder result = new StringBuilder();
        float TotalCost = 0;
        if (this.edges != null) {
            Edge aux = this.edges.get(0);
            sb.append(aux.getStart() + 1).append(",");

            // Build the string representation of edges for the current node
            for (Edge edge : this.edges) {
                sb.append(edge.getDest() + 1).append(", ");
                TotalCost += edge.getWeight();
            }
            //aux = this.bestPath.getEdges().get(this.bestPath.getEdges().size());
            //sb.append(aux.getDest());


            // Remove the trailing comma and space
            if (sb.length() > 2) {
                sb.setLength(sb.length() - 2);
            }

            // Print the formatted output
            result.append("{" + sb.toString() + "} " + TotalCost);

            return result.toString();
        } else return "{}";
    }
}