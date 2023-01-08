/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

package ch.hftm.astrodynamic.physics;

import ch.hftm.astrodynamic.utils.Vector3d;

// calculates density and oxygen content of atmosphere
public interface Atmosphere {
    // density in kg/m^3, absolute position
    double getDensity(Vector3d position);

    // density in kg/m^3, height in m over zero-height mark of celestial body
    double getDensity(double height);

    // percentage constant with altitude, multiply with density to get availability
    double getOxygenPercentage();
}
