/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

package ch.hftm.astrodynamic.physics;

import ch.hftm.astrodynamic.utils.*;

// Can be a planet, moon or asteroid, shape is a sphere
public class Planetoid extends BaseAstronomicalObject implements Atmosphere {

    public Planetoid(double zeroPointHeight, double mass, Vector position, Vector rotation, Vector velocity, Vector rotationalVelocity) {
        super(zeroPointHeight, mass, position, rotation, velocity, rotationalVelocity);
    }

    public Planetoid(Scalar zeroPointHeight, Scalar mass, Vector position, Vector rotation, Vector velocity, Vector rotationalVelocity) {
        super(zeroPointHeight, mass, position, rotation, velocity, rotationalVelocity);
    }

    public Scalar getDensity(Vector position) {
        return new BaseScalar(0);
    }

    public Scalar getDensity(Scalar height) {
        return new BaseScalar(0);
    }

    public Scalar getOxygenPercentage() {
        return new BaseScalar(0);
    }

    // Planetoid is an idealized sphere
    @Override
    public boolean isColliding(Vector offset) {
        return offset.getLength().getValue().le(getZeroElevation().getValue());
    }

    // wind speed in m/s, absolute position 
    public Vector getAtmosphereSpeed(Vector position) {
        return getRotationVelocity();
    }
}
