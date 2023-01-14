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
    
    @Override
    public Scalar multiply(Scalar scalar) throws UnitConversionError  {
        switch (scalar.getUnit()) {
            case MASS:
                return new ForceScalar(getValue().multiply(scalar.getValue()));
            case UNITLESS:
                return new AccelerationScalar(getValue().multiply(scalar.getValue()));
            default:
                throw new UnitConversionError(String.format("Multiplication between %s and %s not possible in %s", getUnit().toString(), scalar.getUnit().toString(), this.getClass().getSimpleName()));
        }
    }
}
