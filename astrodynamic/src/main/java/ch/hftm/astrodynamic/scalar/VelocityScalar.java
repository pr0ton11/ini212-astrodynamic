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
        Quad value = getValue().multiply(scalar.getValue());
        switch (scalar.getUnit()) {
            case MASS:
                return new ForceScalar(value); // velocity * mass = force
            case UNITLESS:
                return new VelocityScalar(value);
            case TIME:
                return new LengthScalar(value); // velocity * time = length (distance)
            case ANGLE:
                return new VelocityScalar(value); // velocity * angle = velocity, intermediate for rotational calculations
            default:
                throw new UnitConversionError(String.format("Multiplication between %s and %s not possible in %s", getUnit().toString(), scalar.getUnit().toString(), this.getClass().getSimpleName()));
        }
    }
}
