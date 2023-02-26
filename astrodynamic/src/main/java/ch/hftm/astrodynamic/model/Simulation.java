package ch.hftm.astrodynamic.model;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ch.hftm.astrodynamic.model.conditions.Condition;
import ch.hftm.astrodynamic.physics.*;
import ch.hftm.astrodynamic.scalar.ScalarFactory;
import ch.hftm.astrodynamic.scalar.TimeScalar;
import ch.hftm.astrodynamic.scalar.UnitlessScalar;
import ch.hftm.astrodynamic.utils.*;

public class Simulation implements Serializable {

    private static final long serialVersionUID = 1L;

    static Scalar UPDATE_TIME_STEP = new TimeScalar(0.5); // we aim to update the simulation once each 0.5 seconds

    Scalar totalTime;

    Spaceship playerControlledVessel; // this is a reference to the controlled vessel, vessel must also be present in spaceships list
    Planetoid referencePlanetoid; // this is a reference, it is used as frame of reference for calculations in gui
    List<Planetoid> planetoids;
    List<Spaceship> spaceships;
    List<Condition> conditions;

    boolean conditionMet;
    Condition metCondition; // we only track the first condition that is met, after that the simulation should end

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

    // slices simulation time into descrete steps for simulation
    public void simulateInSteps(Scalar deltaTime) throws SimulationRuntimeError, UnitConversionError {
        while (deltaTime.gt(UPDATE_TIME_STEP)) {
            simulate(UPDATE_TIME_STEP);
            deltaTime = deltaTime.subtract(UPDATE_TIME_STEP);
        }
        simulate(deltaTime);
    }

    // runs all simulation steps according to time passed since last simulation step
    public void simulate(Scalar deltaTime) throws SimulationRuntimeError, UnitConversionError {

        // simulation ended because of a condition?
        if (isResolved()) {
            return;
        }

        // we can not guarantee correct scalar behaviour if not time
        if (deltaTime.getUnit() != Unit.TIME) {
            throw new UnitConversionError("deltaTime must have unit time");
        }

        // prevent division by zero exceptions before they happen
        if (deltaTime.getValue().le(Quad.ZERO)) {
            throw new SimulationRuntimeError("deltaTime must be greater than zero");
        }

        // reference for more readable access
        List<AstronomicalObject> objects = getAstronomicalObjects();

        // calculate gravity, only calculate with partners we have not met yet
        for (int i = 0; i < objects.size(); i++) {
            for (int j = i + 1; j < objects.size(); j++) {
                Vector force = objects.get(i).calculateGravitationalForce(objects.get(j));

                Vector originalAcceleration = objects.get(i).calculateAccelerationFromForce(force);

                // we calculated the force vector from earth so we need to invert the direction for moon
                Vector partnerAcceleration = objects.get(j).calculateAccelerationFromForce(force.invert());

                // add acceleration to the velocity
                objects.get(i).applyAcceleration(originalAcceleration, deltaTime);
                objects.get(j).applyAcceleration(partnerAcceleration, deltaTime);
            }
        }

        for (AstronomicalObject o: objects)
        {
            // move objects by adding velocity to position
            o.applyVelocity(deltaTime);
        }

        for (Spaceship s: spaceships) {
            s.update(deltaTime); // update internal systems, apply manuevers
        }

        checkConditions();
        
        totalTime = totalTime.add(deltaTime);
    }

    public Scalar getTotalTime() {
        return totalTime;
    }

    public Spaceship getPlayerControlledVessel() {
        return playerControlledVessel;
    }

    // get all objects with gravitational effects
    public List<AstronomicalObject> getAstronomicalObjects() {
        List<AstronomicalObject> objects = new ArrayList<>();
        
        for (Planetoid p: planetoids) {
            objects.add(p);
        }

        for (Spaceship s: spaceships) {
            objects.add(s);
        }

        return objects;
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

    @JsonIgnore
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
    @JsonIgnore
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

    public List<Planetoid> getPlanetoids() {
        return planetoids;
    }

    public List<Spaceship> getSpaceships() {
        return spaceships;
    }

    // check conditons, if one is satisfied we set condtionsMet and reference it
    private void checkConditions() {
        for (Condition c: conditions) {
            if (c.conditionMet(this)) {
                conditionMet = true;
                metCondition = c;
            }
        }
    }

    // have we resolved the simulation by satisfying a condition
    public boolean isResolved() {
        return conditionMet;
    }

    public Condition getMetCondition() {
        return metCondition;
    }

    public String getMetConditionInfo() {
        if (!isResolved()) {
            return "";
        }
        return String.format("%s: %s", getMetCondition().getEndType().toString(), getMetCondition().getDescription());
    }
}
