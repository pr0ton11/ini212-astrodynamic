package ch.hftm.astrodynamic.model;

import ch.hftm.astrodynamic.model.planetoids.Earth;
import ch.hftm.astrodynamic.model.planetoids.Moon;
import ch.hftm.astrodynamic.model.planetoids.Sun;
import ch.hftm.astrodynamic.physics.Spaceship;
import ch.hftm.astrodynamic.utils.Named;

public class Mission extends Simulation implements Named {
    String name;
    String description; // in html

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
