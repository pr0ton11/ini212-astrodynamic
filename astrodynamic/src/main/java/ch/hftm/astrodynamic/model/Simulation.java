package ch.hftm.astrodynamic.model;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

import java.util.*;
import ch.hftm.astrodynamic.physics.*;
import ch.hftm.astrodynamic.scalar.TimeScalar;
import ch.hftm.astrodynamic.utils.*;

public class Simulation {
    private final Quad zeroQuad = new Quad(); // to check against

    Scalar totalTime;

    Spaceship playerControlledVessel; // this is a reference to the controlled vessel, vessel must also be present in spaceships list
    List<Planetoid> planetoids;
    List<Spaceship> spaceships;
    List<Setup> setups;
    List<EndCondition> endings;

    public Simulation() {
        // ArrayLists instead of LinkedLists because number of list manipulations are low but access high
        planetoids = new ArrayList<>();
        spaceships = new ArrayList<>();
        totalTime = new TimeScalar(new Quad());
    }

    // runs once at start of simulation to allow mission to setup
    private void setup() {
        for (Setup s: setups)
            s.setupSimulation(this);
    }

    // runs all simulation steps according to time passed since last simulation step
    public void simulate(Scalar deltaTime) throws SimulationRuntimeError, UnitConversionError {
        // we can not guarantee correct scalar behaviour if not time
        if (deltaTime.getUnit() != Unit.TIME) {
            throw new UnitConversionError("deltaTime must have unit time");
        }

        // prevent division by zero exceptions before they happen
        if (deltaTime.getValue().le(zeroQuad)) {
            throw new SimulationRuntimeError("deltaTime must be greater than zero");
        }

        totalTime = totalTime.add(deltaTime);
    }

    public Scalar getTotalTime() {
        return totalTime;
    }
}
