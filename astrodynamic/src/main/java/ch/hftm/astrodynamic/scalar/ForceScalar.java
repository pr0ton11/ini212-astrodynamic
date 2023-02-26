package ch.hftm.astrodynamic.scalar;

import ch.hftm.astrodynamic.utils.*;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

public class ForceScalar extends BaseScalar {

    public ForceScalar() {
        super(Unit.FORCE);
    }

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
    public Scalar multiply(Scalar scalar) throws UnitConversionError  {
        Quad value = getValue().multiply(scalar.getValue());
        switch (scalar.getUnit()) {
            case UNITLESS:
                return new ForceScalar(value);
            //case ANGLE:
            //    return new ForceScalar(value);
            default:
                throw new UnitConversionError(String.format("Multiplication between %s and %s not possible in %s", getUnit().toString(), scalar.getUnit().toString(), this.getClass().getSimpleName()));
        }
    }

    @Override
    public Scalar divide(Scalar scalar) throws UnitConversionError  {
        Quad value = getValue().divide(scalar.getValue());
        switch (scalar.getUnit()) {
            case UNITLESS:
                return new ForceScalar(value);
            case FORCE:
                return new UnitlessScalar(value);
            case MASS:
                return new AccelerationScalar(value);
            default:
                throw new UnitConversionError(String.format("Division between %s and %s not possible in %s", getUnit().toString(), scalar.getUnit().toString(), this.getClass().getSimpleName()));
        }
    }
}
