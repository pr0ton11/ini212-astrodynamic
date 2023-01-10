package ch.hftm.astrodynamic.utils;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

public class TimeScalar extends BaseScalar {

    public TimeScalar(Scalar scalar) {
        super(scalar, Unit.TIME);
    }

    public TimeScalar(Quad value) {
        super(value, Unit.TIME);
    }

    public TimeScalar(int value) {
        super(value, Unit.TIME);
    }

    public TimeScalar(double value) {
        super(value, Unit.TIME);
    }
    
}
