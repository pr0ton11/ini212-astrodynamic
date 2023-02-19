package ch.hftm.astrodynamic.model;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

import java.util.*;

import ch.hftm.astrodynamic.model.conditions.Condition;
import ch.hftm.astrodynamic.physics.*;
import ch.hftm.astrodynamic.scalar.ScalarFactory;
import ch.hftm.astrodynamic.scalar.TimeScalar;
import ch.hftm.astrodynamic.scalar.UnitlessScalar;
import ch.hftm.astrodynamic.utils.*;

public class Simulation {

    static Scalar UPDATE_TIME_STEP = new TimeScalar(0.1); // we aim to update the simulation once each 0.1 seconds

    Scalar totalTime;

    Spaceship playerControlledVessel; // this is a reference to the controlled vessel, vessel must also be present in spaceships list
    Planetoid referencePlanetoid; // this is a reference, it is used as frame of reference for calculations in gui
    List<Planetoid> planetoids;
    List<Spaceship> spaceships;
    List<Condition> conditions;

    public Simulation() {
        // ArrayLists instead of LinkedLists because number of list manipulations are low but access high
        planetoids = new ArrayList<>();
        spaceships = new ArrayList<>();
        conditions = new ArrayList<>();
        totalTime = new TimeScalar(new Quad());
    }

    /*public void run() {
        Scalar lastTime = ScalarFactory.createFromCurrentMillis();
        Scalar currentTime = new TimeScalar(0);
        while (!this.isInterrupted()) {
            currentTime = ScalarFactory.createFromCurrentMillis();
            try {
                simulate(currentTime.subtract(lastTime));
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            lastTime = currentTime;

            try {
                long sleeptime = Long.getLong(UPDATE_TIME_STEP.subtract(ScalarFactory.createFromCurrentMillis().subtract(currentTime)).multiply(new UnitlessScalar(1000000)).getValue().doubleValue().toString());
                Thread.sleep(sleeptime);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }*/

    // runs once at start of simulation to allow mission to setup
    private void setup() {
        for (Condition s: conditions)
            s.setupSimulation(this);
    }

    // runs all simulation steps according to time passed since last simulation step
    public void simulate(Scalar deltaTime) throws SimulationRuntimeError, UnitConversionError {
        // we can not guarantee correct scalar behaviour if not time
        if (deltaTime.getUnit() != Unit.TIME) {
            throw new UnitConversionError("deltaTime must have unit time");
        }

        // prevent division by zero exceptions before they happen
        if (deltaTime.getValue().le(Quad.ZERO)) {
            throw new SimulationRuntimeError("deltaTime must be greater than zero");
        }

        for (Planetoid p: planetoids) {
                
        }



        totalTime = totalTime.add(deltaTime);
    }

    public Scalar getTotalTime() {
        return totalTime;
    }

    public Spaceship getPlayerControlledVessel() {
        return playerControlledVessel;
    }

    public BaseAstronomicalObject getAstronomicalObjectByName(String name) {
        for (Planetoid p: planetoids) {
            if (p.getName().equals(name)) {
                return p;
            }
        }

        for (Spaceship s: spaceships) {
            if (s.getName().equals(name)) {
                return s;
            }
        }

        return null;
    }

    public boolean removeAstronomicalObjectByName(String name) {
        for (Planetoid p: planetoids) {
            if (p.getName().equals(name)) {
                planetoids.remove(p);
                return true;
            }
        }

        for (Spaceship s: spaceships) {
            if (s.getName().equals(name)) {
                spaceships.remove(s);
                return true;
            }
        }

        return false;
    }

    public List<Named> getAllNamedAstronomicalObjects() {
        List<Named> namedObjects = new ArrayList<>();
        
        for (Planetoid p: planetoids) {
            namedObjects.add(p);
        }

        for (Spaceship s: spaceships) {
            namedObjects.add(s);
        }

        return namedObjects;
    }

    public boolean addPlanetoid(Planetoid planetoid) {
        if (getAstronomicalObjectByName(planetoid.getName()) != null) {
            return false; // we already have something named like this
        }

        planetoids.add(planetoid);

        return true;
    }

    public boolean addSpaceship(Spaceship spaceship) {
        if (getAstronomicalObjectByName(spaceship.getName()) != null) {
            return false; // we already have something named like this
        }

        spaceships.add(spaceship);

        return true;
    }

    public void addCondition(Condition condition) {
        condition.changeSimulationOnAdd(this);
        conditions.add(condition);
    }

    public void removeCondition(Condition condition) {
        if (conditions.remove(condition))
            condition.changeSimulationOnRemove(this);
    }

    // get the list to display in gui, not observable because we want to keep the model as gui agnostic as possible
    public List<Condition> getConditions() {
        return conditions;
    }

    // returns names of planetoids for the ground track gui
    public List<String> getPlanetoidNames() {
        List<String> names = new ArrayList<>();

        for (Planetoid p: planetoids) {
            names.add(p.getName());
        }

        return names;
    }

    public Planetoid getReferencePlanetoid() {
        return referencePlanetoid;
    }

    public void setReferencePlanetoid(Planetoid planetoid) {
        referencePlanetoid = planetoid;
    }

    public void setPlayerControlledVessel(Spaceship spaceship) {
        playerControlledVessel = spaceship;
    }
}
