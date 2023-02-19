
package ch.hftm.astrodynamic.physics;

import ch.hftm.astrodynamic.scalar.LengthScalar;
import ch.hftm.astrodynamic.scalar.UnitlessScalar;

import ch.hftm.astrodynamic.utils.*;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// Can be a planet, moon or asteroid, shape is a sphere
public class Planetoid extends BaseAstronomicalObject implements Atmosphere {

    Scalar atmosphereHeight;
    AtmosphereModel model;

    public Planetoid(double zeroPointHeight, double mass, Vector position, Vector rotation, Vector velocity, Vector rotationalVelocity) {
        super(zeroPointHeight, mass, position, rotation, velocity, rotationalVelocity);
        atmosphereHeight = new LengthScalar();
        model = AtmosphereModel.QUADRATIC_FALLOFF;
    }

    public Planetoid(Scalar zeroPointHeight, Scalar mass, Vector position, Vector rotation, Vector velocity, Vector rotationalVelocity) {
        super(zeroPointHeight, mass, position, rotation, velocity, rotationalVelocity);
        atmosphereHeight = new LengthScalar();
        model = AtmosphereModel.QUADRATIC_FALLOFF;
    }

    public Scalar getDensity(Vector position) {
        return new UnitlessScalar(0);
    }

    public Scalar getDensity(Scalar height) {
        return new UnitlessScalar(0);
    }

    public Scalar getOxygenPercentage() {
        return new UnitlessScalar(0);
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

    public Scalar getAtmosphereHeight() {
        return atmosphereHeight;
    }

    public void setAtmosphereHeight(Scalar height) {
        atmosphereHeight = height;
    }

    public void setAtmosphereModel(AtmosphereModel model){
        this.model = model;
    }

    public AtmosphereModel getAtmosphereModel() {
        return model;
    }
}
