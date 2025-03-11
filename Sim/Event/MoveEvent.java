package Sim.Event;


import AntColony.Ant;
import AntColony.Colony;
import Sim.Stats;

public class MoveEvent extends Event {
    /**
     * Colony of this event
     */
    private Colony colony;
    /**
     * Ant to be moved
     */
    private Ant ant;

    public MoveEvent(double step, Colony colony, Ant ant) {
        super(step);
        this.colony = colony;
        this.ant = ant;
    }

    /**
     * Implemented method from abstract class that executes the move
     */
    public Event execute() {

        return colony.move(ant, super.getStep());

    }

    @Override
    public Event execute(Stats stats, Colony colony) {
        return null;
    }

    /**
     * Getter method for the colony
     *
     * @return Colony for this event
     */
    public Colony getColony() {
        return colony;
    }

    /**
     * Setter method for the colony
     *
     * @param colony Colony to be assigned
     */
    public void setColony(Colony colony) {
        this.colony = colony;
    }

    /**
     * Getter method for the Ant
     *
     * @return Ant moved in this event
     */
    public Ant getAnt() {
        return ant;
    }

    /**
     * Setter method for the Ant
     *
     * @param ant ant to be used by the event
     */
    public void setAnt(Ant ant) {
        this.ant = ant;
    }


}
