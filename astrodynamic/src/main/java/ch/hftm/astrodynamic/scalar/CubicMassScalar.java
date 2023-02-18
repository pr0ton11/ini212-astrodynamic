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

public class CubicMassScalar extends BaseScalar {

    public CubicMassScalar(Scalar scalar) {
        super(scalar, Unit.CUBIC_MASS);
    }

    public CubicMassScalar(Quad value) {
        super(value, Unit.CUBIC_MASS);
    }

    public CubicMassScalar(int value) {
        super(value, Unit.CUBIC_MASS);
    }

    public CubicMassScalar(double value) {
        super(value, Unit.CUBIC_MASS);
    }
    
    @Override
    public Scalar multiply(Scalar scalar) throws UnitConversionError  {
        switch (scalar.getUnit()) {
            case UNITLESS:
                return new CubicMassScalar(getValue().multiply(scalar.getValue()));
            default:
                throw new UnitConversionError(String.format("Multiplication between %s and %s not possible in %s", getUnit().toString(), scalar.getUnit().toString(), this.getClass().getSimpleName()));
        }
    }

    @Override
    public Scalar divide(Scalar scalar) throws UnitConversionError {
        Quad divisionResult = this.getValue().divide(scalar.getValue());
        switch(scalar.getUnit()) {
            case AREA:
                return new M2divL2Scalar(divisionResult); // joins the dimensions into the cubic mass / area scalar for further calculations
            case MASS:
                return new MassScalar(divisionResult); // cubic mass / mass = mass
            case UNITLESS:
                return new CubicMassScalar(divisionResult); // cubic mass / unitless = cubic mass
            case CUBIC_MASS:
                return new UnitlessScalar(divisionResult); // cubic mass / cubic mass = unitless
            default:
                throw new UnitConversionError(String.format("%s can not be divided by %s", getClass().getSimpleName(), scalar.getUnit().toString()));
        }
    }
}