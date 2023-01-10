package ch.hftm.astrodynamic.utils;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

public class ForceScalar extends BaseScalar {

    public ForceScalar(Scalar scalar) {
        super(scalar, Unit.FORCE);
    }

    public ForceScalar(Quad value) {
        super(value, Unit.FORCE);
    }

    public ForceScalar(int value) {
        super(value, Unit.FORCE);
    }

    public ForceScalar(double value) {
        super(value, Unit.FORCE);
    }
    
}
