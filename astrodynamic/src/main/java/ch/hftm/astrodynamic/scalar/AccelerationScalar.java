package ch.hftm.astrodynamic.scalar;

import ch.hftm.astrodynamic.utils.BaseScalar;
import ch.hftm.astrodynamic.utils.Quad;
import ch.hftm.astrodynamic.utils.Scalar;
import ch.hftm.astrodynamic.utils.Unit;
import ch.hftm.astrodynamic.utils.UnitConversionError;

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
        Quad value = getValue().multiply(scalar.getValue());
        switch (scalar.getUnit()) {
            case UNITLESS:
                return new AccelerationScalar(value);
            case TIME:
                return new VelocityScalar(value);
            default:
                throw new UnitConversionError(String.format("Multiplication between %s and %s not possible in %s", getUnit().toString(), scalar.getUnit().toString(), this.getClass().getSimpleName()));
        }
    }
}
