package ch.hftm.astrodynamic.model;

import java.io.Serializable;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// changing simulation at loadtime
public interface Setup extends Serializable {
    void setupSimulation(Simulation sim);
}
