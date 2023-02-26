package ch.hftm.astrodynamic.physics;

import ch.hftm.astrodynamic.utils.*;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// Spaceship encapsulates all controllable astronomical objects without atmosphere but with drag calculation
public class Spaceship extends BaseAstronomicalObject implements Drag {

    private static Quad MINIMUM_DENSITY = new Quad(1.0, -11); // minimum density before calculation rounds of to zero

    public Spaceship(double zeroPointHeight, double mass, Vector position, Vector rotation, Vector velocity, Vector rotationalVelocity) {
        super(zeroPointHeight, mass, position, rotation, velocity, rotationalVelocity);
    }

    public Spaceship(Scalar zeroPointHeight, Scalar mass, Vector position, Vector rotation, Vector velocity, Vector rotationalVelocity) {
        super(zeroPointHeight, mass, position, rotation, velocity, rotationalVelocity);
    }

    @Override
    public Scalar getDragCoefficient(Vector directionThroughMedium) {
        return null;
    }

    // returns frontal area in m² according to the direction vector
    @Override
    public Scalar getFrontArea(Vector directionThroughMedium) {
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
}
