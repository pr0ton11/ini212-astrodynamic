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

public class MassScalar extends BaseScalar {

    public MassScalar() {
        super(Unit.MASS);
    }

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
        Quad multiplicationResult = getValue().multiply(scalar.getValue());
        switch (scalar.getUnit()) {
            case VELOCITY:
                return new ForceScalar(multiplicationResult);
            case MASS:
                return new CubicMassScalar(multiplicationResult);
            case UNITLESS:
                return new MassScalar(multiplicationResult);
            default:
                throw new UnitConversionError(String.format("Multiplication between %s and %s not possible in %s", getUnit().toString(), scalar.getUnit().toString(), this.getClass().getSimpleName()));
        }
    }
}
