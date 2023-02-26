package ch.hftm.astrodynamic.physics;

import java.io.Serializable;

import ch.hftm.astrodynamic.utils.*;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// Spaceship encapsulates all controllable astronomical objects without atmosphere but with drag calculation
public class Spaceship extends BaseAstronomicalObject implements Drag, Serializable {

    private static final long serialVersionUID = 1L;

    private static Quad MINIMUM_DENSITY = new Quad(1.0, -11); // minimum density before calculation rounds of to zero

    private Scalar deltaV; // maximum possible velocity change
    private Scalar acceleration; // acceleration per second
    private Vector burn; // states planned velocity change
    private boolean maneuvering; // quick check if burn calculations are needed

    public Spaceship(double zeroPointHeight, double mass, Vector position, Vector rotation, Vector velocity, Vector rotationalVelocity) {
        super(zeroPointHeight, mass, position, rotation, velocity, rotationalVelocity);
    }

    public Spaceship(Scalar zeroPointHeight, Scalar mass, Vector position, Vector rotation, Vector velocity, Vector rotationalVelocity) {
        super(zeroPointHeight, mass, position, rotation, velocity, rotationalVelocity);
    }

    @Override
    public Scalar getDragCoefficient(Vector directionThroughMedium) {
        // TODO Auto-generated method stub
        return null;
    }

    // returns frontal area in m² according to the direction vector
    @Override
    public Scalar getFrontArea(Vector directionThroughMedium) {
        // TODO Auto-generated method stub
        return null;
    }

    // drag coefficient formula with reynolds number, 1/2 C p v² a
    @Override
    public Vector getDragForce(Planetoid partner) throws UnitConversionError {
        // get atmos density from planet on our position
        Scalar density = partner.getDensity(getPosition());

        // return zero if density is below minimum
        if (density.getValue().le(MINIMUM_DENSITY)) {
            return new BaseVector(Unit.FORCE);
        }

        // we now get the velocity difference between atmosphere and the ship
        Vector rotationVelocity = partner.getAtmosphereSpeed(getPosition());
        Vector velocityDifference = getVelocity().subtract(rotationVelocity);

        // get drag coefficient and frontal area from the velocity direction
        Vector dragDirection = new BaseVector(velocityDifference, Unit.UNITLESS);
        Scalar dragCoefficient = getDragCoefficient(dragDirection);
        Scalar frontalArea = getFrontArea(dragDirection);

        return null;
    }

    public Scalar getDeltaV() {
        return deltaV;
    }

    public void setDeltaV(Scalar deltaV) {
        this.deltaV = deltaV;
    }

    public Scalar getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Scalar acceleration) {
        this.acceleration = acceleration;
    }

    public Vector getBurn() {
        return burn;
    }

    public void setBurn(Vector burn) {
        this.burn = burn;
        maneuvering = true;
    }

    public boolean isManeuvering() {
        return maneuvering;
    }

    // updates internal systems, used for maneuvering
    public void update(Scalar time) throws UnitConversionError {
        if (!maneuvering) {
            return;
        }

        Scalar maxVelocity = getAcceleration().multiply(time);

        if (burn.getLength().ge(deltaV)) { // planned burn greater than max delta V budget, reduce it
            burn = burn.normalize().multiply(deltaV); // scaled to available budged
        }

        if (burn.getLength().le(maxVelocity)) { // we have less burn todo than we could achieve
            deltaV = deltaV.subtract(burn.getLength()); // subract burn from deltaV budget
            setVelocity(getVelocity().add(burn)); // add burn to our velocity
            maneuvering = false; // we finished the maneuver
            return;
        }

        Vector partialBurn = burn.normalize().multiply(maxVelocity); // scale burn direction with maximum achievable delta V in timeframe
        setVelocity(getVelocity().add(partialBurn));
        burn = burn.subtract(partialBurn); // reduce burn
        deltaV = deltaV.subtract(partialBurn.getLength()); // subract burn from deltaV budget
    }
}
