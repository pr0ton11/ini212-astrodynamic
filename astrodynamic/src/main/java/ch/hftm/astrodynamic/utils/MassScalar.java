package ch.hftm.astrodynamic.utils;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

public class MassScalar extends BaseScalar {

    public MassScalar(Scalar scalar) {
        super(scalar, Unit.MASS);
    }

    public MassScalar(Quad value) {
        super(value, Unit.MASS);
    }

    public MassScalar(int value) {
        super(value, Unit.MASS);
    }

    public MassScalar(double value) {
        super(value, Unit.MASS);
    }
    
}
