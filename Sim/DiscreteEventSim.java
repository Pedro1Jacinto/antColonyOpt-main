package Sim;

import AntColony.Colony;
import Board.Edge;
import Sim.Event.*;
import Sim.Event.StatsEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Discrete Event Simulator for an Ant Colony
 */
public class DiscreteEventSim implements Sim {

    /**
     * Current Simulation Step
     */
    private double currentStep;

    /**
     * Max Simulation Step
     */
    private double maxStep;

    /**
     * FIFO Queue of events to be handled during the simulation
     */
    private PriorityQueue<Event> events;

    /**
     * Ant Colony to which the events are applied
     */
    private Colony colony;

    /**
     * Statistics of the simulation
     */
    private Stats stats;

    /**
     * Constructor Method for the DiscreteEventSim Class
     *
     * @param input Input parsed from the command line
     */
    public DiscreteEventSim(String[] input) {

        if ((input.length == 12) || (input.length == 2)) {
            switch (input[0]) {

                case "-r": {
                    this.currentStep = 0;
                    this.maxStep = Double.parseDouble(input[11]);
                    startSimulation(input);
                    break;
                }
                case "-f": {
                    try {
                        File file = new File(input[1]);
                        Scanner scanner = new Scanner(file);
                        if (scanner.hasNextLine()) {
                            String firstLine = scanner.nextLine();

                            String[] line1 = firstLine.split(" ");
                            this.maxStep = Double.parseDouble(line1[9]);
                            startSimulation(line1, file);
                            break;
                        }
                    } catch (FileNotFoundException e) {
                        System.out.println(("File not found: " + e.getMessage()));
                    }
                }
                default: {
                    System.out.println("Invalid mode");
                    System.exit(0);
                }
            }
        } else {
            System.out.println("Invalid mode");
            System.exit(0);
        }
    }

    /**
     * Method that sets up the simulation but does not run it
     *
     * @param input Input from the command line
     */
    public void startSimulation(String[] input) {
        /*Create colony and ants with events*/
        events = new PriorityQueue<>(Integer.parseInt(input[10]) + 1);
        stats = new Stats(this.currentStep, Double.parseDouble(input[11]));
        events.add(new StatsEvent(stats.getInterval(), stats));
        colony = new Colony(input);
        for (int i = 0; i < colony.getAnts().size(); i++) {
            events.add(new MoveEvent(0, colony, colony.getAnts().get(i)));
        }
    }

    public void startSimulation(String[] line1, File file) {

        events = new PriorityQueue<>(Integer.parseInt(line1[8]) + 1);
        stats = new Stats(this.currentStep, Double.parseDouble(line1[9]));
        events.add(new StatsEvent(stats.getInterval(), stats));
        colony = new Colony(line1, file);
        for (int i = 0; i < colony.getAnts().size(); i++) {
            events.add(new MoveEvent(0, colony, colony.getAnts().get(i)));
        }
    }

    /**
     * Actual run of the simulation, runs until no more event are found in the queue
     */
    public void runSimulation() {
        do {
            try {
                Event current = this.exeNextEvent();

                setCurrentStep(current.getStep());
                stats.setStep(currentStep);

                if (current instanceof MoveEvent) {
                    stats.countMoveEvents();
                } else if (current instanceof EvapEvent) {
                    stats.countEvapEvents();
                } else if (current instanceof BestPathEvent) {
                    current.execute(stats, colony);
                    for (Edge aux : ((BestPathEvent) current).getAnt().getPath().getEdges()) {
                        addEvent(new EvapEvent(this.currentStep, aux, this.colony)); //TODO Exponential time  for evap events
                    }
                    ((BestPathEvent) current).getAnt().reset(this.colony.getN1());
                    addEvent(new MoveEvent(this.currentStep, this.colony, ((BestPathEvent) current).getAnt()));
                }
            } catch (NoSuchElementException | NullPointerException e1) {
                System.out.println(e1.getMessage());
                return;
            }


        } while (currentStep - maxStep <= 0);

        System.out.println("\n\n\n\tSimulation has reached it's end.\n \tFinal Statistics are:");
        stats.printStats();

    }

    /**
     * Gets the current Simulation Step
     *
     * @return double containing the current simulation step
     */
    public double getCurrentStep() {
        return currentStep;
    }

    /**
     * Sets the current Simulation Step
     *
     * @param currentStep Value to be assigned
     */
    public void setCurrentStep(double currentStep) {
        this.currentStep = currentStep;
    }

    /**
     * Gets the Maximum Simulation Step
     *
     * @return double containing the max simulation time
     */
    public double getMaxStep() {
        return maxStep;
    }

    /**
     * Sets the maximum Simulation Step
     *
     * @param maxStep Value to be assigned
     */
    public void setMaxStep(double maxStep) {
        this.maxStep = maxStep;
    }

    /**
     * Executes the next Event on the queue
     *
     * @return The Event that was just executed
     * @throws NoSuchElementException For when the queue is empty
     */
    public Event exeNextEvent() throws NoSuchElementException {
        try {
            Event event = events.poll();
            try {
                this.setCurrentStep(event.getStep());
                Event next = event.execute();
                if (!(next instanceof BestPathEvent)) {
                    if (next != null) {
                        addEvent(next);
                    }
                    return event;
                } else return next;
            } catch (NullPointerException e1) {
                return null;
            }
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Adds an event to the queue
     *
     * @param event Event to be added
     */
    public void addEvent(Event event) {
        this.events.offer(event);
    }
}
