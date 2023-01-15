package ch.hftm.astrodynamic.physics;

import ch.hftm.astrodynamic.utils.Scalar;
import ch.hftm.astrodynamic.utils.Vector;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// calculation for object which is influenced by atmospheric drag
public interface Drag {

    // returns unitless drag coefficient according to the direction vector
    Scalar getDragCoefficient(Vector directionThroughMedium);

    // returns force vector from drag from planetoid atmosphere
    Vector getDragForce(Planetoid partner);
}