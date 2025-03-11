package Sim.Event;

import AntColony.Ant;
import AntColony.Colony;
import Sim.Stats;

public class BestPathEvent extends Event {
    /**
     * Best Path to be tried
     */
    private Ant ant;

    /**
     * Constructor method for the Best Path Event
     *
     * @param step Step to be executed
     * @param ant  ant that completed a cycle
     */
    public BestPathEvent(double step, Ant ant) {
        super(step);
        this.ant = ant;
    }

    @Override
    public Event execute() {
        return null;
    }

    @Override
    public Event execute(Stats stats, Colony colony) {
        if (stats.getBestPath() != null) {
            if ((ant.getPath().sumWeight() < stats.getBestPath().sumWeight()) || (stats.getBestPath().sumWeight() < 0)) {
                stats.newBestPath(ant.getPath());
            }
        } else stats.setBestPath(ant.getPath());

        return new MoveEvent(super.getStep(), colony, ant);
    }

    /**
     * Gets the ant that found the best path
     *
     * @return Ant
     */
    public Ant getAnt() {
        return ant;
    }
}
