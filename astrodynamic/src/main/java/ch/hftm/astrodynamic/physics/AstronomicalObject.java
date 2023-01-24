/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

package ch.hftm.astrodynamic.physics;

import ch.hftm.astrodynamic.utils.*;

// Base attributes to define atronomical object with gravity
public interface AstronomicalObject {
    Scalar getMass();
    Vector getPosition();
    Vector getVelocity();
    Vector getRotation();
    Vector getRotationVelocity();

    void setMass(Scalar mass);
    void setPosition(Vector position);
    void setVelocity(Vector velocity);
    void setRotation(Vector rotation);
    void setRotationVelocity(Vector rotationVelocity); 

    // returns the zero elevation height in meters
    Scalar getZeroElevation();

    // returns true if offset point from center is filled with the collision shape
    boolean isColliding(Vector offset);

    // returns collision obj with all collision data
    Collision calculateCollision(AstronomicalObject partner) throws UnitConversionError;

    // returns Force vector of gravitational interaction
    Vector calculateGravitationalForce(AstronomicalObject partner) throws UnitConversionError;

    // returns acceleration from force
    Vector calculateAccelerationFromForce(Vector force) throws UnitConversionError;
}
