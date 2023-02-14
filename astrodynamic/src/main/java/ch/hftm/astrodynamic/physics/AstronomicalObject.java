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

    Vector getDirection(AstronomicalObject partner) throws UnitConversionError;

    Scalar getDistance(AstronomicalObject partner) throws UnitConversionError;

    Vector calculateOrbitalSpeed(AstronomicalObject partner) throws UnitConversionError;

    void applyAcceleration(Vector velocity, Scalar time) throws UnitConversionError;

    void applyVelocity(Scalar time) throws UnitConversionError;

    // returns -1.0 to 1.0 to denote the difference to the position and rotation
    double getGrundtrackFactorX(AstronomicalObject partner);

    // returns -1.0 to 1.0 to denote the difference to the position and rotation
    double getGroundtrackFactorY(AstronomicalObject partner);
}
