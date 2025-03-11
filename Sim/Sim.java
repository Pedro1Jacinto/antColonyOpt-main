package Sim;

import Sim.Event.Event;

import java.util.NoSuchElementException;

/**
 * Interface for Simulating over time
 */
public interface Sim {
    /**
     * Sets up the simulation but doesn't start it
     * @param input Input Parameters for the Simulation
     */
    public void startSimulation(String[] input);

    /**
     * A Complete run of the simulation, either until max time or until there are no more events to be handled
     */
    public void runSimulation();

    /**
     * Execution of the next event in the list
     * @return The event that was just executed
     * @throws NoSuchElementException When the queue is empty
     */
    public Event exeNextEvent() throws NoSuchElementException;
}
