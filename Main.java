import Sim.DiscreteEventSim;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// thenpress Enter. You can now see whitespace characters in your code.


public class Main {
    public static void main(String[] args) {

        DiscreteEventSim sim = new DiscreteEventSim(args);
        System.out.println("\nSimulation is ready, starting\n");
        sim.runSimulation();
        System.out.println("Thank you, goobbye!");
        System.exit(0);

    }
}