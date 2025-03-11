package AntColony;

import Board.Edge;
import Board.Graph;
import Sim.Event.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Implements the Colony used in the simulation
 */
public class Colony {
    /**
     * Ant colony size
     */
    protected int AntSize;
    /**
     * Nest node
     */
    protected int n1;
    /**
     * Parameter concerning ant movement
     */
    protected float alpha;
    /**
     * Parameter concerning ant movement
     */
    protected float beta;
    /**
     * Parameter concerning ant movement
     */
    protected float delta;
    /**
     * Parameter concernig the pheromone evaporation event
     */
    protected float eta;
    /**
     * Parameter concernig the pheromone evaporation event
     */
    protected float rho;
    /**
     * Parameter concernig the pheromone evaporation event
     */
    protected float lambda;
    /**
     * List of Ants in the colony
     */
    private ArrayList<Ant> ants;
    /**
     * Pheromone matrix where the existing edges weights are substituted
     * by the level of pheromone in the edge
     */
    private float[][] pheromoneMatrix;
    /**
     * Graph Class with all Edges
     */
    private Graph graph;

    /**
     * Constructor of Colony when given one string as input, it creates the graph
     * and reads all important parameters from the command line and generates a
     * random graph
     *
     * @param input Input from the Terminal
     */
    public Colony(String[] input) {

        graph = new Graph(input[1], input[2]); // number of nodes and max weight
        graph.printGraph();
        CreatePheromoneGraph(graph);
        setN1(Integer.parseInt(input[3]) - 1);
        setAlpha(Float.parseFloat(input[4]));
        setBeta(Float.parseFloat(input[5]));
        setDelta(Float.parseFloat(input[6]));
        setEta(Float.parseFloat(input[7]));
        setRho(Float.parseFloat(input[8]));
        setLambda(Float.parseFloat(input[9]));
        setAntSize(Integer.parseInt(input[10]));

        PrintParameters(Float.parseFloat(input[11]));

        ants = new ArrayList<>(AntSize);
        for (int i = 0; i < AntSize; i++) {
            ants.add(i, new Ant(n1));
        }
    }

    /**
     * Constructor of colony when the input is a string and a file, reads the
     * important parameters from the first line of the file and then proceeds to create
     * the graph given in the file
     *
     * @param line1
     * @param file
     */
    public Colony(String[] line1, File file) {
        graph = new Graph(file);
        CreatePheromoneGraph(graph);
        setN1(Integer.parseInt(line1[1]));
        setAlpha(Float.parseFloat(line1[2]));
        setBeta(Float.parseFloat(line1[3]));
        setDelta(Float.parseFloat(line1[4]));
        setEta(Float.parseFloat(line1[5]));
        setRho(Float.parseFloat(line1[6]));
        setLambda(Float.parseFloat(line1[7]));
        setAntSize(Integer.parseInt(line1[8]));


        PrintParameters(Float.parseFloat(line1[9]));
        graph.printGraph();
        List<Edge> edges = graph.getNodeEdges(0); //TODO Remove
        for (Edge edge : edges) {
            System.out.println(edge);
        }

        ants = new ArrayList<>(AntSize);
        for (int i = 0; i < AntSize; i++) {
            ants.add(i, new Ant(n1));
        }


    }

    /**
     * Method that is responsible for the evaporation of the pheromones in
     * the edges, it updates the pheromone matrix
     *
     * @param edge
     * @param currentStep
     * @return Event Evap or Null
     */
    public Event evap(Edge edge, double currentStep) {

        Random random = new Random();

        if (this.pheromoneMatrix[edge.getStart()][edge.getDest()] > 0) {
            if (this.pheromoneMatrix[edge.getStart()][edge.getDest()] < this.rho) {
                this.pheromoneMatrix[edge.getStart()][edge.getDest()] = 0;
                this.pheromoneMatrix[edge.getDest()][edge.getStart()] = 0;
                return null;
            } else {
                this.pheromoneMatrix[edge.getStart()][edge.getDest()] -= this.rho;
                this.pheromoneMatrix[edge.getDest()][edge.getStart()] -= this.rho;

                double next = -lambda * Math.log(1 - random.nextDouble()); // Exponential distribution
                return new EvapEvent(currentStep + next, edge, this);
            }
        } else return null;
    }

    /**
     * Method responsible for the ant movement, it reads the next possible moves
     * for the ant, calculates their probability of happening and then creates an event
     * to visit the highest one
     *
     * @param ant
     * @param currentStep
     * @return Event Move
     */
    public Event move(Ant ant, double currentStep) {

        float total = 0;
        Random random = new Random();
        Edge chosen;
        AntPath possible = new AntPath();

        possible.setEdges(graph.getNodeEdges(ant.getCurrentNode()));

        possible.getEdges().removeAll(ant.getPath().getEdges());

        float[] J = new float[possible.getEdges().size()];
        float[] P = new float[possible.getEdges().size()];
        float[] sum = new float[possible.getEdges().size()];

        for (int i = 0; i < possible.getEdges().size(); i++) {

            J[i] = getProb(getaAlpha(), getBeta(), possible.getEdges().get(i).getStart(), possible.getEdges().get(i).getDest());
            total += J[i];
        }

        if (total == 0) {

            chosen = graph.getNodeEdges(ant.getCurrentNode()).get(random.nextInt(graph.getNodeEdges(ant.getCurrentNode()).size()));
        } else {
            P[0] = J[0] / total;
            sum[0] = P[0];
            for (int i = 1; i < J.length; i++) {
                P[i] = J[i] / total;
                sum[i] = P[i] + sum[i - 1];
            }
            chosen = possible.getEdges().get(getMaxProbIndex(sum));
        }

        ant.setCurrentNode(chosen.getDest());
        ant.appendPath(chosen, graph.getNumV());

        if (ant.isHamiltonCycle(graph.getNumV())) {
            pheromonize(ant);
            //ant.reset(n1);
            return new BestPathEvent(currentStep, ant);
        } else {
            double next = -chosen.getWeight() * delta * Math.log(1 - random.nextDouble());
            return new MoveEvent(currentStep + next, this, ant);
        }
    }

    /**
     * Spreads pheromones in a path
     * @param ant Ant
     */
    public void pheromonize(Ant ant) {
        for (int i = 0; i < ant.getPath().getEdges().size(); i++) {
            pheromoneMatrix[ant.getPath().getEdges().get(i).getStart()][ant.getPath().getEdges().get(i).getDest()] = (float) (lambda * graph.getTotalWeight()) / ant.getPath().sumWeight();
            pheromoneMatrix[ant.getPath().getEdges().get(i).getDest()][ant.getPath().getEdges().get(i).getStart()] = (float) (lambda * graph.getTotalWeight()) / ant.getPath().sumWeight();
        }
    }

    /**
     * Method that returns the probability of a move happening
     *
     * @param alpha
     * @param beta
     * @param pheromone
     * @param weight
     * @return probability
     */
    public float getProb(float alpha, float beta, float pheromone, float weight) {

        float num = alpha + pheromone;
        float denum = beta + weight;

        return num / denum;
    }

    /**
     * Method that returns the index of where the highest probability is in the array
     *
     * @param J
     * @return index
     */
    public int getMaxProbIndex(float[] J) {

        Random random = new Random();
        double uniformRandom = random.nextDouble();

        for (int i = 0; i < J.length; i++) {
            if (J[i] < uniformRandom) return i;
        }
        return J.length - 1;
    }

    /**
     * Sets ant colony size value
     *
     * @param Size
     */
    public void setAntSize(int Size) {
        this.AntSize = Size;
    }

    /**
     * Sets nest value
     *
     * @param Nest
     */
    public void setN1(int Nest) {
        this.n1 = Nest;
    }

    /**
     * Sets alpha value
     *
     * @param Alpha
     */
    public void setAlpha(float Alpha) {
        this.alpha = Alpha;
    }

    /**
     * Sets beta value
     *
     * @param Beta
     */
    public void setBeta(float Beta) {
        this.beta = Beta;
    }

    /**
     * Sets delta value
     *
     * @param Delta
     */
    public void setDelta(float Delta) {
        this.delta = Delta;
    }

    /**
     * Sets eta value
     *
     * @param Eta
     */
    public void setEta(float Eta) {
        this.eta = Eta;
    }

    /**
     * sets rho value
     *
     * @param Rho
     */
    public void setRho(float Rho) {
        this.rho = Rho;
    }

    /**
     * sets lambda value
     *
     * @param Lambda
     */

    public int getN1() {
        return this.n1;
    }

    public void setLambda(float Lambda) {
        this.lambda = Lambda;
    }

    /**
     * Method that returns the ant colony size
     *
     * @return
     */
    public int getAntSize() {
        return this.AntSize;
    }

    /**
     * Method that returns the value of alpha
     *
     * @return alpha
     */
    public float getaAlpha() {
        return this.alpha;
    }

    /**
     * Method that returns the value of beta
     *
     * @return beta
     */
    public float getBeta() {
        return this.beta;
    }

    /**
     * Method that returns the value of delta
     *
     * @return delta
     */
    public float getDelta() {
        return this.delta;
    }

    /**
     * Method that returns the value of eta
     *
     * @return eta
     */
    public float getEta() {
        return this.eta;
    }

    /**
     * Method that returns the value of rho
     *
     * @return rho
     */
    public float getRho() {
        return this.rho;
    }

    /**
     * Method that returns the value of lambda
     *
     * @return lambda
     */
    public float getLambda() {
        return this.rho;
    }

    /**
     * Method that returns the list of ants
     *
     * @return
     */
    public ArrayList<Ant> getAnts() {
        return ants;
    }

    /**
     * Method that creates the pheromone matrix
     *
     * @param normalGraph
     * @return pheromone matrix
     */
    public void CreatePheromoneGraph(Graph normalGraph) {
        pheromoneMatrix = new float[graph.getNumV()][graph.getNumV()];
        for (int i = 0; i < normalGraph.getNumV(); i++) {
            for (int j = 0; j < normalGraph.getNumV(); j++) {
                pheromoneMatrix[i][j] = -1;
            }
        }
        for (int k = 0; k < normalGraph.getNumV(); k++) {
            List<Edge> edges = normalGraph.getNodeEdges(k + 1);
            for (Edge edge : edges) {
                if (pheromoneMatrix[edge.getStart()][edge.getDest()] == -1) {
                    pheromoneMatrix[edge.getStart()][edge.getDest()] = 0;
                    pheromoneMatrix[edge.getDest()][edge.getStart()] = 0;
                }
            }
        }
    }

    /**
     * Prints input parameters
     * @param tau flaot
     */
    public void PrintParameters(float tau){

        System.out.println("n: "+this.graph.getNumV());
        System.out.println("n1: "+this.getN1());
        System.out.println("alpha: "+this.getaAlpha());
        System.out.println("beta: "+this.getBeta());
        System.out.println("delta: "+this.getDelta());
        System.out.println("eta: "+this.getEta());
        System.out.println("rho: "+this.getRho());
        System.out.println("lambda: "+this.getLambda());
        System.out.println("Size of colony: "+this.getAntSize());
        System.out.println("Final instant: " + tau);
    }

}
