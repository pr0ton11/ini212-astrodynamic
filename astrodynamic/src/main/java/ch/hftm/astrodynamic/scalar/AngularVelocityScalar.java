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

public class AngularVelocityScalar extends BaseScalar {

    public AngularVelocityScalar() {
        super(Unit.ANGULAR_VELOCITY);
    }

    public AngularVelocityScalar(Scalar scalar) {
        super(scalar, Unit.ANGULAR_VELOCITY);
    }

    public AngularVelocityScalar(Quad value) {
        super(value, Unit.ANGULAR_VELOCITY);
    }

    public AngularVelocityScalar(int value) {
        super(value, Unit.ANGULAR_VELOCITY);
    }

    public AngularVelocityScalar(double value) {
        super(value, Unit.ANGULAR_VELOCITY);
    }
    
}
