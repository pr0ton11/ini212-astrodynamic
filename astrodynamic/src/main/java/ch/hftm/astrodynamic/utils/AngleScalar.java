package ch.hftm.astrodynamic.utils;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

public class AngleScalar extends BaseScalar {

    public AngleScalar(Scalar scalar) {
        super(scalar, Unit.ANGLE);
    }

    public AngleScalar(Quad value) {
        super(value, Unit.ANGLE);
    }

    public AngleScalar(int value) {
        super(value, Unit.ANGLE);
    }

    public AngleScalar(double value) {
        super(value, Unit.ANGLE);
    }
    
}
