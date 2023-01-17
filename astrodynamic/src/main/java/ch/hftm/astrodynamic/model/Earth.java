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

/* planet earth 
* mass: 5.97219e+24 kg
* zero point: 6.371e+6 m
* TODO: axial tilt
*/
public class Earth extends Planetoid {
    public Earth() {
        super(new LengthScalar(new Quad(6.371).multiply(new Quad(10).pow(6))), new MassScalar(new Quad(5.97219).multiply(new Quad(10).pow(24))), new BaseVector(Unit.LENGTH), new BaseVector(Unit.ANGLE), new BaseVector(Unit.VELOCITY), new BaseVector(Unit.ANGULAR_VELOCITY));
    }
}
