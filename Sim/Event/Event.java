package Sim.Event;

import AntColony.Colony;
import Sim.Stats;

/***
 * Abstract class for events running on this simulator package;
 */
public abstract class Event implements Comparable<Event> {
    /**
     * Step of the simulation to run the event
     */
    private double step;

    /**
     * Constructor method to be called by super()
     * @param step Step to run the event on
     */
    public Event(double step) {
        this.step = step;
    }
    /**
     * Compares the step of two events
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(Event o) {
        return (int) (this.step - o.step);
    }

    /**
     * Gets the step for the Event
     * @return
     */
    public double getStep() {
        return step;
    }

    /**
     * Execution of said event
     * @return An event created as a cause of running this event
     */
    public abstract Event execute();

    public abstract Event execute(Stats stats, Colony colony);
}
