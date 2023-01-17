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
        super(0, Unit.LENGTH);
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
        switch (scalar.getUnit()) {
            case LENGTH:
                return new AreaScalar(getValue().multiply(scalar.getValue()));
            case AREA:
                return new VolumeScalar(getValue().multiply(scalar.getValue()));
            case UNITLESS:
                return new LengthScalar(getValue().multiply(scalar.getValue()));
            default:
                throw new UnitConversionError(String.format("Multiplication between %s and %s not possible in length scalar", getUnit().toString(), scalar.getUnit().toString()));
        }
    }

    @Override
    public Scalar divide(Scalar scalar) throws UnitConversionError  {
        switch (scalar.getUnit()) {
            case UNITLESS:
                return new LengthScalar(getValue().divide(scalar.getValue()));
            case LENGTH:
                return new BaseScalar(getValue().divide(scalar.getValue()));
            default:
                throw new UnitConversionError(String.format("Division between %s and %s not possible in length scalar", getUnit().toString(), scalar.getUnit().toString()));
        }
    }
}
