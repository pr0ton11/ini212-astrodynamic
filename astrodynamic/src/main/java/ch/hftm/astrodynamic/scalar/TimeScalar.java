package ch.hftm.astrodynamic.scalar;

import ch.hftm.astrodynamic.utils.BaseScalar;
import ch.hftm.astrodynamic.utils.Quad;
import ch.hftm.astrodynamic.utils.Scalar;
import ch.hftm.astrodynamic.utils.Unit;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

public class TimeScalar extends BaseScalar {

    public TimeScalar() {
        super(Unit.TIME);
    }

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
