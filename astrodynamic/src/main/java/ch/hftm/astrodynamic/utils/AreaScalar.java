package ch.hftm.astrodynamic.utils;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

public class AreaScalar extends BaseScalar {

    public AreaScalar(Scalar scalar) {
        super(scalar, Unit.AREA);
    }

    public AreaScalar(Quad value) {
        super(value, Unit.AREA);
    }

    public AreaScalar(int value) {
        super(value, Unit.AREA);
    }

    public AreaScalar(double value) {
        super(value, Unit.AREA);
    }
    
}
