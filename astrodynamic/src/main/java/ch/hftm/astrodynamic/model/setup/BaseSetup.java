package ch.hftm.astrodynamic.model.setup;

import ch.hftm.astrodynamic.model.Setup;
import ch.hftm.astrodynamic.utils.Named;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// setup prepares the simulation according to the mission
public abstract class BaseSetup implements Named, Setup {
    String name;
    String description;
    
    BaseSetup(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // should only be accessible for subclasses
    protected void setDescription(String description) {
        this.description = description;
    }
}
