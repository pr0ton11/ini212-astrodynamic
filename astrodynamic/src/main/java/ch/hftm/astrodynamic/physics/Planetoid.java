
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
    AtmosphereModel atmosphereModel;
    Scalar atmosphereOxygenFactor;

    public Planetoid(Scalar zeroPointHeight, Scalar mass, Vector position, Vector rotation, Vector velocity, Vector rotationalVelocity, Scalar atmosphereHeight, AtmosphereModel atmosphereModel, Scalar atmosphereOxygenFactor) {
        super(zeroPointHeight, mass, position, rotation, velocity, rotationalVelocity);
        this.atmosphereHeight = atmosphereHeight;
        this.atmosphereModel = atmosphereModel;
        this.atmosphereOxygenFactor = atmosphereOxygenFactor;
    }

    public Planetoid(double zeroPointHeight, double mass, Vector position, Vector rotation, Vector velocity, Vector rotationalVelocity) {
        super(zeroPointHeight, mass, position, rotation, velocity, rotationalVelocity);
        atmosphereHeight = new LengthScalar();
        atmosphereModel = AtmosphereModel.QUADRATIC_FALLOFF;
        atmosphereOxygenFactor = new UnitlessScalar(0.0);
    }

    public Planetoid(Scalar zeroPointHeight, Scalar mass, Vector position, Vector rotation, Vector velocity, Vector rotationalVelocity) {
        super(zeroPointHeight, mass, position, rotation, velocity, rotationalVelocity);
        atmosphereHeight = new LengthScalar();
        atmosphereModel = AtmosphereModel.QUADRATIC_FALLOFF;
        atmosphereOxygenFactor = new UnitlessScalar(0.0);
    }

    public Scalar getDensity(Vector position) {
        return new UnitlessScalar(0);
    }

    public Scalar getDensity(Scalar height) {
        return new UnitlessScalar(0);
    }

    public Scalar getOxygenFactor() {
        return atmosphereOxygenFactor;
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
        this.atmosphereModel = model;
    }

    public AtmosphereModel getAtmosphereModel() {
        return atmosphereModel;
    }

    public void setOxygenFactor(Scalar oxygenFactor) {
        this.atmosphereOxygenFactor = oxygenFactor;
    }
    
}
