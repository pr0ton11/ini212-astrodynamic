package ch.hftm.astrodynamic.model;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

import ch.hftm.astrodynamic.physics.*;
import ch.hftm.astrodynamic.scalar.LengthScalar;
import ch.hftm.astrodynamic.scalar.MassScalar;
import ch.hftm.astrodynamic.utils.*;

/* planetoid moon (luna) 
* mass: 7.34767309e+22 kg
* zero point: 1.7374e+6 m
* TODO: axial tilt
*/
public class Moon extends Planetoid {
    public Moon() {
        super(new LengthScalar(new Quad(1.7374).multiply(new Quad(10).pow(6))), new MassScalar(new Quad(7.34767309).multiply(new Quad(10).pow(22))), new BaseVector(Unit.LENGTH), new BaseVector(Unit.ANGLE), new BaseVector(Unit.VELOCITY), new BaseVector(Unit.ANGULAR_VELOCITY));
    }
}
