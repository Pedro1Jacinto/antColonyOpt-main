package Sim;

import AntColony.AntPath;
import Board.Edge;
import Sim.Event.StatsEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that handles the statistics of the simulation
 */
public class Stats {
    /**
     * Current Step of the simulation
     */
    private double step;
    /**
     * Maximum Step of the simulation
     */
    private final double maxStep;
    /**
     * Interval between prints of the statistics
     */
    private final double interval;
    /**
     * Number of Move Events that occured
     */
    private double moveEvents;
    /**
     * Number of Evaporation Events that occured
     */
    private double evapEvents;
    /**
     * Best Path to transverse the entire graph currently
     */
    private AntPath bestPath;
    /**
     * Top Cycles after best path
     */
    private List<AntPath> topCycles;

    /**
     * Constructor Method for the Stats class
     *
     * @param step    Current Step of the simulation
     * @param maxStep Maxmimum Step of the simulation
     */
    public Stats(double step, double maxStep) {
        this.step = step;
        this.maxStep = maxStep;
        this.interval = maxStep / 20;
        this.moveEvents = 0;
        this.evapEvents = 0;
        this.bestPath = null;
        this.topCycles = new ArrayList<>(5);

    }

    /**
     * Prints the stats to the command line
     *
     * @return An event for the next print
     */
    public StatsEvent printStats() {
        System.out.println("--------------------------------------------------------------------");
        System.out.printf("Present instant:    %.0f\n", step);
        System.out.println("Number of Move Events:  " + moveEvents);
        System.out.println("Number of Evaporation Events:   " + evapEvents);
        System.out.print("Top Candidate Cycles:   ");
        for (AntPath topCycle : topCycles) {
            System.out.println(topCycle.toString());
        }
        if (bestPath == null) {
            System.out.println("Best Hamiltonian Cycle:     {}");
        } else {
            System.out.println("Best Hamiltonian Cycle: " + bestPath);
        }
        System.out.println("--------------------------------------------------------------------");
        if (step + interval < maxStep) {
            return new StatsEvent(step + interval, this);
        } else if (step < maxStep) return new StatsEvent(maxStep, this);
        else return null;
    }

    public List<AntPath> getTopCycles() {
        return topCycles;
    }

    public void setTopCycles(List<AntPath> topCycles) {
        this.topCycles = topCycles;
    }

    public void appendTopCycles(AntPath newCycle) {
        return;
    }

    /**
     * Sets the current simulation step
     *
     * @param step
     */
    public void setStep(double step) {
        this.step = step;
    }

    /**
     * Gets the best Path currently
     *
     * @return AntPath
     */
    public AntPath getBestPath() {
        return bestPath;
    }

    /**
     * Sets the bestPath
     *
     * @param bestPath Path to set
     */
    public void setBestPath(AntPath bestPath) {
        this.bestPath = bestPath.clone();
    }

    /**
     * Handler for a new bestPath or top cycle
     *
     * @param newBest AntPath
     */
    public void newBestPath(AntPath newBest) {

        if ((newBest.sumWeight() < bestPath.sumWeight()) || (bestPath.sumWeight() < 0)) {
            bestPath = newBest.clone();
        } else {
            for (int i = 0; i < 5 && i < topCycles.size(); i++) {
                if (topCycles.get(i).sumWeight() > bestPath.sumWeight()) {
                    topCycles.add(i, bestPath.clone());
                }
            }
        }

    }


    /**
     * Adds 1 to the number of Move Events occurred to this point
     */
    public void countMoveEvents() {
        moveEvents++;
    }

    /**
     * Adds 1 to the number of Evaporation Events occurred to this point
     */
    public void countEvapEvents() {
        evapEvents++;
    }

    /**
     * Gets the interval between prints
     *
     * @return double
     */
    public double getInterval() {
        return interval;
    }
}
