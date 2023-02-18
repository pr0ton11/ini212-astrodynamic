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

public class LengthScalar extends BaseScalar {

    public LengthScalar() {
        super(Unit.LENGTH);
    }

    public LengthScalar(Scalar scalar) {
        super(scalar, Unit.LENGTH);
    }

    public LengthScalar(Quad value) {
        super(value, Unit.LENGTH);
    }

    public LengthScalar(int value) {
        super(value, Unit.LENGTH);
    }

    public LengthScalar(double value) {
        super(value, Unit.LENGTH);
    }
    
    @Override
    public Scalar multiply(Scalar scalar) throws UnitConversionError  {
        Quad newValue = getValue().multiply(scalar.getValue());
        switch (scalar.getUnit()) {
            case LENGTH:
                return new AreaScalar(newValue);
            case AREA:
                return new VolumeScalar(newValue);
            case UNITLESS:
                return new LengthScalar(newValue);
            case F_L2_Mn2:
                return new VelocityScalar(newValue);
            default:
                throw new UnitConversionError(String.format("Multiplication between %s and %s not possible in length scalar", getUnit().toString(), scalar.getUnit().toString()));
        }
    }

    @Override
    public Scalar divide(Scalar scalar) throws UnitConversionError  {
        Quad newValue = getValue().divide(scalar.getValue());
        switch (scalar.getUnit()) {
            case UNITLESS:
                return new LengthScalar(newValue);
            case LENGTH:
                return new UnitlessScalar(newValue);
            default:
                throw new UnitConversionError(String.format("Division between %s and %s not possible in length scalar", getUnit().toString(), scalar.getUnit().toString()));
        }
    }
}
