package ch.hftm.astrodynamic.utils;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// named thing with description, for gui things like missions, parameters, and such
public interface Named {
    String getName();
    String getDescription();
    void setName(String name);
    void setDescription(String description);
}
