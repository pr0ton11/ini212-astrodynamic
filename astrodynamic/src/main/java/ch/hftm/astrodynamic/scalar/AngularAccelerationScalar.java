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

public class AngularAccelerationScalar extends BaseScalar {

    public AngularAccelerationScalar(Scalar scalar) {
        super(scalar, Unit.ANGULAR_ACCELERATION);
    }

    public AngularAccelerationScalar(Quad value) {
        super(value, Unit.ANGULAR_ACCELERATION);
    }

    public AngularAccelerationScalar(int value) {
        super(value, Unit.ANGULAR_ACCELERATION);
    }

    public AngularAccelerationScalar(double value) {
        super(value, Unit.ANGULAR_ACCELERATION);
    }
    
}
