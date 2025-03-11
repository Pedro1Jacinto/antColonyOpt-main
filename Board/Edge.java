package Board;

import java.util.List;
import java.util.Objects;

/**
 * Implements the edges of the graph
 */
public class Edge {
    /**
     * The node the edge is from
     */
    protected int start;
    /**
     * The destination of the edge
     */
    protected int destination;
    /**
     * The weight of the edge
     */
    protected float weight;

    /**
     * Constructor of Edge given the start, the destination and the weight
     * to creates the edge
     *
     * @param start       One of the nodes of the edge
     * @param destination The other node
     * @param weight      Weight of crossing the edge
     */
    public Edge(int start, int destination, float weight) {
        setDest(destination);
        setStart(start);
        setWeight(weight);
    }

    /**
     * Method that sets the start of the edge
     *
     * @param start One node of the edge
     */
    private void setStart(int start) {

        this.start = start;
    }

    /**
     * Method that returns the start of the edge
     *
     * @return start One node of the edge
     */
    public int getStart() {
        return this.start;
    }

    /**
     * Method that sets the destination of the edge
     *
     * @param dest Destination to set
     */
    private void setDest(int dest) {
        this.destination = dest;
    }

    /**
     * Method that returns the destination of the edge
     *
     * @return destination
     */
    public int getDest() {
        return this.destination;
    }

    /**
     * Method that sets the weight of the edge
     *
     * @param wei Weight to set
     */
    private void setWeight(float wei) {
        this.weight = wei;
    }

    /**
     * Method that returns the weight of the edge
     *
     * @return weight
     */
    public float getWeight() {
        return this.weight;
    }

    /**
     * Compares two edges
     *
     * @param path Edge to compare to this
     * @return True if the same, false otherwise
     */
    public boolean compareTo(Edge path) {

        if (path.getDest() == this.destination && path.getStart() == this.start) {
            return true;
        }
        if (path.getDest() == this.start && path.getStart() == this.destination) {
            return true;
        } else return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;

        if (start == edge.start && destination == edge.destination) return true;
        else return start == edge.destination && destination == edge.start;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, destination, weight);
    }

    @Override
    public String toString() {
        return "Origin: " + (getStart() + 1) + ", To: " + (getDest() + 1) + ", Weight= " + getWeight();
    }

}

