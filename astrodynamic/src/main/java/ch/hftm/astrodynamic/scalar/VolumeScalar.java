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

public class VolumeScalar extends BaseScalar {

    public VolumeScalar() {
        super(Unit.VOLUME);
    }

    public VolumeScalar(Scalar scalar) {
        super(scalar, Unit.VOLUME);
    }

    public VolumeScalar(Quad value) {
        super(value, Unit.VOLUME);
    }

    public VolumeScalar(int value) {
        super(value, Unit.VOLUME);
    }

    public VolumeScalar(double value) {
        super(value, Unit.VOLUME);
    }
    
}
