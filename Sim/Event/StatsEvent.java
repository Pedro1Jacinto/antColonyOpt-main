package Sim.Event;

import AntColony.Colony;
import Sim.Stats;

/**
 * Implements an event as the printing of the statistics
 */
public class StatsEvent extends Event {
    /**
     * Reference to the Statistics Object for this simulation
     */
    private Stats stats;

    /**
     * Constructor method for the StatsEvent Class
     *
     * @param step  Current Step of the simulation
     * @param stats Statistics Object for this Simulation
     */
    public StatsEvent(double step, Stats stats) {
        super(step);
        this.stats = stats;
    }

    /**
     * Execution of the event
     *
     * @return The new event of printing the statistics
     */
    public Event execute() {
        return stats.printStats();
    }

    @Override
    public Event execute(Stats stats, Colony colony) {
        return stats.printStats();
    }


}
