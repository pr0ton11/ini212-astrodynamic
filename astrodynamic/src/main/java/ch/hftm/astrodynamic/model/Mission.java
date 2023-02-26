package ch.hftm.astrodynamic.model;

import java.io.Serializable;

import ch.hftm.astrodynamic.model.planetoids.Earth;
import ch.hftm.astrodynamic.model.planetoids.Moon;
import ch.hftm.astrodynamic.model.planetoids.Sun;
import ch.hftm.astrodynamic.utils.Named;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */


public class Mission extends Simulation implements Named, Serializable {

    String name;
    String description; // in html

    private static final long serialVersionUID = 1L;

    public Mission() {
        this("", "");
    }

    public Mission(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }

    public void setupStandardSolarSystem() {
        planetoids.clear();

        Sun.addToSimulation(this);
        Earth.addToSimulation(this);
        Moon.addToSimulation(this);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
}
