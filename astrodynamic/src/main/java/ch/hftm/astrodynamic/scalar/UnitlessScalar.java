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

public class UnitlessScalar extends BaseScalar {

    public UnitlessScalar(Scalar scalar) {
        super(scalar, Unit.UNITLESS);
    }

    public UnitlessScalar(Quad value) {
        super(value, Unit.UNITLESS);
    }

    public UnitlessScalar(int value) {
        super(value, Unit.UNITLESS);
    }

    public UnitlessScalar(double value) {
        super(value, Unit.UNITLESS);
    }
    
    @Override
    public Scalar multiply(Scalar scalar) throws UnitConversionError  {
        // the unit result will always be the unit of the multiplied scalar
        return ScalarFactory.create(getValue().multiply(scalar.getValue()), scalar.getUnit());
    }

    @Override
    public Scalar divide(Scalar scalar) throws UnitConversionError  {
        switch (scalar.getUnit()) {
            case UNITLESS:
                return new UnitlessScalar(getValue().divide(scalar.getValue()));
            default:
                throw new UnitConversionError(String.format("Division between %s and %s not possible in %s", getUnit().toString(), scalar.getUnit().toString(), this.getClass().getSimpleName()));
        }
    }
}