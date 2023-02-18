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

 // Scalar for intermediate calculation steps, N * Area * Mass⁻²
public class FL2Mn2Scalar extends BaseScalar {

    public FL2Mn2Scalar(Scalar scalar) {
        super(scalar, Unit.F_L2_Mn2);
    }

    public FL2Mn2Scalar(Quad value) {
        super(value, Unit.F_L2_Mn2);
    }

    public FL2Mn2Scalar(int value) {
        super(value, Unit.F_L2_Mn2);
    }

    public FL2Mn2Scalar(double value) {
        super(value, Unit.F_L2_Mn2);
    }
    
    @Override
    public Scalar multiply(Scalar scalar) throws UnitConversionError  {
        switch (scalar.getUnit()) {
            case UNITLESS:
                return new FL2Mn2Scalar(getValue().multiply(scalar.getValue()));
            case M2_DIV_L2:
                return new ForceScalar(getValue().multiply(scalar.getValue()));
            default:
                throw new UnitConversionError(String.format("Multiplication between %s and %s not possible in %s", getUnit().toString(), scalar.getUnit().toString(), this.getClass().getSimpleName()));
        }
    }
}