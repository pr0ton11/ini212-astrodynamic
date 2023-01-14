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
    
    @Override
    public Scalar multiply(Scalar scalar) throws UnitConversionError  {
        switch (scalar.getUnit()) {
            case MASS:
                return new ForceScalar(getValue().multiply(scalar.getValue()));
            case UNITLESS:
                return new VelocityScalar(getValue().multiply(scalar.getValue()));
            default:
                throw new UnitConversionError(String.format("Multiplication between %s and %s not possible in %s", getUnit().toString(), scalar.getUnit().toString(), this.getClass().getSimpleName()));
        }
    }
}
