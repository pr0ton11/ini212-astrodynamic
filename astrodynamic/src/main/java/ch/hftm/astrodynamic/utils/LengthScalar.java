package ch.hftm.astrodynamic.utils;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

public class LengthScalar extends BaseScalar {

    public LengthScalar(Scalar scalar) {
        super(scalar, Unit.LENGTH);
    }

    public LengthScalar(Quad value) {
        super(value, Unit.LENGTH);
    }

    public LengthScalar(int value) {
        super(value, Unit.LENGTH);
    }

    public LengthScalar(double value) {
        super(value, Unit.LENGTH);
    }
    
}
