package ch.hftm.astrodynamic.utils;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// Exception unexpected situation in simulation
public class SimulationRuntimeError extends Exception {
    
    // Constructor
    public SimulationRuntimeError(String msg) {
        super(String.format("SimulationRuntimeError: %s", msg));
    }

}
