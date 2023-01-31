package ch.hftm.astrodynamic.model;
/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// can end simulation
public interface EndCondition {
    // returns true if contions are met
    boolean conditionMet(Simulation sim);

    // returns how the mission ended if conditions are met
    EndType getEndType();
}
