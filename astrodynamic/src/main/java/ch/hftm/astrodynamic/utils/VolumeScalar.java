package ch.hftm.astrodynamic.utils;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

public class VolumeScalar extends BaseScalar {

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
