package ch.hftm.astrodynamic.physics;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

import ch.hftm.astrodynamic.utils.*;

public class Spaceship extends BaseAstronomicalObject implements Drag {

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

    @Override
    public Vector getDragForce(Planetoid partner) {
        // TODO Auto-generated method stub
        return null;
    }
}
