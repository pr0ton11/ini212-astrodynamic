package ch.hftm.astrodynamic.model.conditions;

import ch.hftm.astrodynamic.model.EndCondition;
import ch.hftm.astrodynamic.model.Setup;
import ch.hftm.astrodynamic.model.Simulation;
import ch.hftm.astrodynamic.utils.Named;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// condition needs naming for gui, end conditions to check for its win/loss and setup to create the scenario to check against
public abstract class BaseCondition implements Named, EndCondition, Setup {
    String name;
    String description;
    
    BaseCondition() {
        this.name = getClass().getSimpleName(); // easy naming for reflection
        this.description = "";
    }

    BaseCondition(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    // implemented here to avoid clutter in specific implementations
    public void setupSimulation(Simulation sim) {};
}
