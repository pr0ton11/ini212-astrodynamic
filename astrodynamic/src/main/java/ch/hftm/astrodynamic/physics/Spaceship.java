package ch.hftm.astrodynamic.physics;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

import ch.hftm.astrodynamic.utils.*;

public class Spaceship extends BaseAstronomicalObject implements Drag {

    private static Quad MINIMUM_DENSITY = new Quad(1).multiply(new Quad(10).pow(-11));

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
