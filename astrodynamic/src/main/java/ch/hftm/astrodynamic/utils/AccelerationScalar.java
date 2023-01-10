package ch.hftm.astrodynamic.utils;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

public class AccelerationScalar extends BaseScalar {

    public AccelerationScalar(Scalar scalar) {
        super(scalar, Unit.ACCELERATION);
    }

    public AccelerationScalar(Quad value) {
        super(value, Unit.ACCELERATION);
    }

    public AccelerationScalar(int value) {
        super(value, Unit.ACCELERATION);
    }

    public AccelerationScalar(double value) {
        super(value, Unit.ACCELERATION);
    }
    
}
