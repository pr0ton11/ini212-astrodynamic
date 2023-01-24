package ch.hftm.astrodynamic.model;

import ch.hftm.astrodynamic.physics.Spaceship;

public class Mission implements Named {
    String name;
    String description; // in html
    Spaceship playerControlledVessel; // this is a reference to the controlled vessel

    public Mission(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
