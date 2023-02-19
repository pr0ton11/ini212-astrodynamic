
package ch.hftm.astrodynamic.physics;

import ch.hftm.astrodynamic.utils.*;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// calculates density and oxygen content of atmosphere
public interface Atmosphere {
    // density in kg/m^3, absolute position
    Scalar getDensity(Vector position);

    // density in kg/m^3, height in m over zero-height mark of celestial body
    Scalar getDensity(Scalar height);

    // percentage constant with altitude, multiply with density to get availability
    Scalar getOxygenPercentage();

    // wind speed in m/s, absolute position 
    Vector getAtmosphereSpeed(Vector position);

    // length over zero elevation, calculation cut off point
    Scalar getAtmosphereHeight();

    // length over zero elevation, calculation cut off point
    void setAtmosphereHeight(Scalar height);

    void setAtmosphereModel(AtmosphereModel model);

    AtmosphereModel getAtmosphereModel();
}
