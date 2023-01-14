package ch.hftm.astrodynamic.utils;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

public class ForceScalar extends BaseScalar {

    public ForceScalar(Scalar scalar) {
        super(scalar, Unit.FORCE);
    }

    public ForceScalar(Quad value) {
        super(value, Unit.FORCE);
    }

    public ForceScalar(int value) {
        super(value, Unit.FORCE);
    }

    public ForceScalar(double value) {
        super(value, Unit.FORCE);
    }
    
    @Override
    public Scalar multiply(Scalar scalar) throws UnitConversionError {
        Quad product = getValue().multiply(scalar.getValue());
        switch (scalar.getUnit()) {
            case UNITLESS:
                return new ForceScalar(product);
            default:
                throw new UnitConversionError(String.format("Multiplication between %s and %s not possible in %s", getUnit().toString(), scalar.getUnit().toString(), getClass().getSimpleName()));
        }
    }
}
