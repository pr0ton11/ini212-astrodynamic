package ch.hftm.astrodynamic.utils;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

public class M2divL2Scalar extends BaseScalar {

    public M2divL2Scalar(Scalar scalar) {
        super(scalar, Unit.M2_DIV_L2);
    }

    public M2divL2Scalar(Quad value) {
        super(value, Unit.M2_DIV_L2);
    }

    public M2divL2Scalar(int value) {
        super(value, Unit.M2_DIV_L2);
    }

    public M2divL2Scalar(double value) {
        super(value, Unit.M2_DIV_L2);
    }
    
    @Override
    public Scalar multiply(Scalar scalar) throws UnitConversionError  {
        switch (scalar.getUnit()) {
            case UNITLESS:
                return new M2divL2Scalar(getValue().multiply(scalar.getValue()));
            case F_L2_Mn2:
                return new ForceScalar(getValue().multiply(scalar.getValue()));
            default:
                throw new UnitConversionError(String.format("Multiplication between %s and %s not possible in %s", getUnit().toString(), scalar.getUnit().toString(), this.getClass().getSimpleName()));
        }
    }
}