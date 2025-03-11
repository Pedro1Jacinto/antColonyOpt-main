package Sim.Event;

import AntColony.Colony;
import Board.Edge;
import Sim.Stats;

/**
 * Implementation of the abstract Event Class geared towards Evaporation Events
 */
public class EvapEvent extends Event {
    private Edge edge;
    private Colony colony;

    /**
     * Constructor Method for the Evaporation Event
     *
     * @param step   Step to make the evaporation
     * @param edge   Edge in which to apply the evaporation
     * @param colony Colony evaporating
     */
    public EvapEvent(double step, Edge edge, Colony colony) {
        super(step);
        this.edge = edge;
        this.colony = colony;
    }

    /**
     * Execution of the Evaporation
     *
     * @return Next Evaporation Event for this edge
     */
    public Event execute() {
        return colony.evap(edge, super.getStep());
    }

    @Override
    public Event execute(Stats stats, Colony colony) {
        return null;
    }
}