package ch.hftm.astrodynamic.utils;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

 // Factory to create specific scalars depending on Unit
public class ScalarFactory {

    // returns specific scalar class instance depending on unit
    public static Scalar create(Quad value, Unit unit) throws UnitConversionError {
        switch (unit) {
            case UNITLESS:
                return new BaseScalar(value);
            case LENGTH:
                return new LengthScalar(value);
            case AREA:
                return new AreaScalar(value);
            case VOLUME:
                return new VolumeScalar(value);
            case ANGLE:
                return new AngleScalar(value);
            case FORCE:
                return new ForceScalar(value);
            case MASS:
                return new MassScalar(value);
            case TIME:
                return new TimeScalar(value);
            case VELOCITY:
                return new VelocityScalar(value);
            case ACCELERATION:
                return new AccelerationScalar(value);
            default:
                throw new UnitConversionError(String.format("Unit %s not supported", unit.toString()));
        }
    }

    // wrapper to create with int
    public static Scalar create(int value, Unit unit) throws UnitConversionError {
        return ScalarFactory.create(new Quad(value), unit);
    }

    // wrapper to create with double
    public static Scalar create(double value, Unit unit) throws UnitConversionError {
        return ScalarFactory.create(new Quad(value), unit);
    }

    // wrapper to create from other scalar value
    public static Scalar create(Scalar scalar, Unit unit) throws UnitConversionError {
        return ScalarFactory.create(new Quad(scalar.getValue()), unit);
    }
}
