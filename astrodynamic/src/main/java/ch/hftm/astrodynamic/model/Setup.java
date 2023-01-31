package ch.hftm.astrodynamic.model;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// changing simulation at loadtime
public interface Setup {
    void setupSimulation(Simulation sim);
}
