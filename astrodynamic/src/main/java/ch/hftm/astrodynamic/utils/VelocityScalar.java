package ch.hftm.astrodynamic.utils;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

public class VelocityScalar extends BaseScalar {

    public VelocityScalar(Scalar scalar) {
        super(scalar, Unit.VELOCITY);
    }

    public VelocityScalar(Quad value) {
        super(value, Unit.VELOCITY);
    }

    public VelocityScalar(int value) {
        super(value, Unit.VELOCITY);
    }

    public VelocityScalar(double value) {
        super(value, Unit.VELOCITY);
    }
    
}
