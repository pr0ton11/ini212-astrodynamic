/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

package ch.hftm.astrodynamic.physics;

import ch.hftm.astrodynamic.utils.Vector3d;

// holds all necessary data from a collison between two collision shapes
public class Collision {
    CollisionShape shapeA;
    CollisionShape shapeB;
    double impactEnergy; // in Newton
    Vector3d impactPointFromA;
    Vector3d impactPointFromB;
}
