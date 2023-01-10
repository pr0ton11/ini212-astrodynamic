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
    Vector3d getPosition();
    Vector3d getVelocity();
    Vector3d getRotation();
    Vector3d getRotationVelocity();

    void setMass(Scalar mass);
    void setPosition(Vector3d position);
    void setVelocity(Vector3d velocity);
    void setRotation(Vector3d rotation);
    void setRotationVelocity(Vector3d rotationVelocity); 

    // returns the zero elevation height in meters
    Scalar getZeroElevation();

    // returns true if offset point from center is filled with the collision shape
    boolean isColliding(Vector3d offset);

    // returns collision obj with all collision data
    Collision calculateCollision(AstronomicalObject partnerShape) throws UnitConversionError;
}
