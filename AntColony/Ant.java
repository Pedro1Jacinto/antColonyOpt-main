package AntColony;

import Board.Edge;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements each Ant that belongs to the Colony
 */
public class Ant {
    /**
     * Current node where the Ant is present at the moment
     */
    private int currentNode;
    /**
     * The path the Ant has done so far
     */
    private AntPath path;

    /**
     * Constructor of Ant Class
     */
    Ant(int nest) {
        currentNode = nest;
        this.path = new AntPath();
    }

    /**
     * Method that returns the sum of all the nodes of a graph
     *
     * @param numNodes num of nodes in the graph
     * @return integer that is the sum of all nodes
     */
    private int getHamiltonPathSignature(int numNodes) {
        int graphsignature = 0;

        for (int i = 0; i < numNodes; i++) {
            graphsignature = graphsignature + i;
        }
        return graphsignature;
    }

    /**
     * Method that converts a antpath to a list
     *
     * @return List interpretation of a antpath
     */
    private List<Integer> convertPathToList() {
        List<Integer> temp = new ArrayList<Integer>();

        temp.add(path.getEdges().get(0).getStart());
        temp.add(path.getEdges().get(0).getDest());

        if (path.getEdges().size() == 1) return temp;

        for (int i = 1; i < path.getEdges().size(); i++) {
            temp.add(path.getEdges().get(i).getDest());
        }

        return temp;
    }

    /**
     * Method that gives the sum of all nodes between a section of a antpath represented as a list
     *
     * @param pathList antpath represented as a list of integers
     * @param start    starting index
     * @param end      ending index
     */
    private int getPathSignature(List<Integer> pathList, int start, int end) {
        int count = 0;

        for (int i = start; i < end; i++) {
            count = count + pathList.get(i);
        }

        return count;
    }

    /**
     * Method the says if in the Ant path there is a premature cycle
     *
     * @param numNodes num of nodes of the graph
     * @return boolean that says states if there is a premature cycle
     */
    private boolean isCycle(int numNodes) {
        List<Integer> pathList = convertPathToList();

        int endOfLoop = pathList.get(pathList.size() - 1);

        // Verifica se existem ciclos
        // Verifica se é um ciclo de hamilton
        if (isHamiltonCycle(numNodes)) return false;

        // Verifica se existem outros ciclos prematuros
        for (int i = pathList.size() - 2; i >= 0; i--) {
            if (pathList.get(i) == endOfLoop) return true;
        }

        return false;
    }

    /**
     * Method that verifies if the current path is a Hamilton Cycle
     *
     * @param numNodes num of nodes of the graph
     * @return boolean that states if the path is a hamilton cycle
     */
    public boolean isHamiltonCycle(int numNodes) {
        if (path.getEdges().size() == 0) return false;

        List<Integer> pathList = convertPathToList();

        if (pathList.get(0) != pathList.get(pathList.size() - 1))
            return false;
        return getHamiltonPathSignature(numNodes) == getPathSignature(pathList, 0, pathList.size() - 1);
    }

    /**
     * Method that removes a premature Cycle form the path
     */
    private void removePrematureCycle() {
        List<Integer> pathList = convertPathToList();
        List<Integer> resultList;

        int endOfLoop = pathList.get(pathList.size() - 1);
        int startOfLoop = 0;

        // Verifica se existem outros ciclos prematuros
        for (int i = pathList.size() - 2; i >= 0; i--) {
            if (pathList.get(i) == endOfLoop) {
                startOfLoop = i;
                break;
            }
        }

        resultList = pathList.subList(0, startOfLoop + 1);
        currentNode = pathList.get(pathList.size() - 1);
        path.setEdges(path.getEdges().subList(0, resultList.size() - 1));
    }

    /**
     * Method that returns the currentNode of the Ant
     */
    public int getCurrentNode() {
        return currentNode;
    }

    /**
     * Method that sets the currentNode of the Ant
     *
     * @param currentNode The current node that the ant is at
     */
    public void setCurrentNode(int currentNode) {
        this.currentNode = currentNode;
    }

    /**
     * Method that returns the antpath
     *
     * @return AntPath object of the Ant
     */
    public AntPath getPath() {
        return path;
    }

    /**
     * Method that sets the path
     *
     * @param antPath The new AntPath of the Ant
     */
    public void setPath(AntPath antPath) {
        path = antPath;

        try {
            currentNode = path.getEdges().get(path.getEdges().size() - 1).getDest();
        } catch (IndexOutOfBoundsException ignored) {
        }

    }

    /**
     * Method the appends a new edge to the current path of the ant.
     * It also verifies if the path is alright
     *
     * @param newNode  Is the new Edge to be added in the Ant path
     * @param numNodes Number of nodes in the graph
     */
    public void appendPath(Edge newNode, int numNodes) {

        path.appendPath(newNode);
        // Verifies if the ant has made a cycle
        // If it finds it removes it
        if (isCycle(numNodes)) {
            // Removes the cycle
            removePrematureCycle();
        }
    }

    /**
     * Method that clear the path of the Ant and resets the Ant to the nest
     *
     * @param nest Nest node of the graph
     */
    public void reset(int nest) {
        currentNode = nest;
        path.getEdges().clear();
    }
}
