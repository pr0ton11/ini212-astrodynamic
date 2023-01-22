package ch.hftm.astrodynamic.model;

public class Mission {
    String name;
    String description; // in html

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
