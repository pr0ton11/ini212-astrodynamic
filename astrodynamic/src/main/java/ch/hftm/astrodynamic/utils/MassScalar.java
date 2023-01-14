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
    
    @Override
    public Scalar multiply(Scalar scalar) throws UnitConversionError  {
        switch (scalar.getUnit()) {
            case ACCELERATION:
                return new ForceScalar(getValue().multiply(scalar.getValue()));
            case UNITLESS:
                return new MassScalar(getValue().multiply(scalar.getValue()));
            default:
                throw new UnitConversionError(String.format("Multiplication between %s and %s not possible in %s", getUnit().toString(), scalar.getUnit().toString(), this.getClass().getSimpleName()));
        }
    }
}
