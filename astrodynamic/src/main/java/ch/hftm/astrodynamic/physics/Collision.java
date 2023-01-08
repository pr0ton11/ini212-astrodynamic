/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

package ch.hftm.astrodynamic.physics;

import ch.hftm.astrodynamic.utils.Vector3d;

// holds all necessary data from a collison between two collision shapes
public class Collision {
    public AstronomicalObject shapeA;
    public AstronomicalObject shapeB;
    public double impactEnergy; // in Newton
    public Vector3d impactPointFromA;
    public Vector3d impactPointFromB;
}
