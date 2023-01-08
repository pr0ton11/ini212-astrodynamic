/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

package ch.hftm.astrodynamic.physics;

import ch.hftm.astrodynamic.utils.Vector3d;

// To check collisions between objects
public interface CollisionShape {
    // returns collision obj with all collision data
    Collision calculateCollision(CollisionShape partnerShape);

    // basic check
    boolean isColliding(CollisionShape partnerShape);

    // returns collision energy in Newtons to determine severity/damage
    double calculateEnergyOnCollision(CollisionShape partnerShape);

    // returns percise point (measured in meters) where the collision occured relative to our shape center
    Vector3d calculateImpactPoint(CollisionShape partnerShape);

    // returns the zero elevation height in meters
    double getZeroElevation();
}
