package ch.hftm.astrodynamic.scalar;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import ch.hftm.astrodynamic.utils.Quad;
import ch.hftm.astrodynamic.utils.Scalar;
import ch.hftm.astrodynamic.utils.Unit;
import ch.hftm.astrodynamic.utils.UnitConversionError;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

 // Factory to create specific scalars depending on Unit
public class ScalarFactory {

    static final Map<Unit, Map<String, Quad>> conversions;
    static {
        conversions = new HashMap<>();
        Map<String, Quad> timeConversion = new HashMap<>();
        timeConversion.put("seconds", new Quad(1.0));
        timeConversion.put("minutes", new Quad(60.0));
        timeConversion.put("hours", new Quad(60.0 * 60.0));
        timeConversion.put("days", new Quad(24.0 * 60.0 * 60.0));
        timeConversion.put("weeks", new Quad(7.0 * 24.0 * 60.0 * 60.0));
        conversions.put(Unit.TIME, timeConversion);

        Map<String, Quad> lengthConversion = new HashMap<>();
        lengthConversion.put("meter", new Quad(1.0));
        lengthConversion.put("kilometer", new Quad(1000.0));
        lengthConversion.put("AU", new Quad(1.495978707).multiply(Quad.TEN.pow(11)));
        conversions.put(Unit.LENGTH, lengthConversion);
    }

    // helper gets unit for a scalar class
    public static Unit getUnitFromClass(Class scalarClass) {
        System.out.println(scalarClass.toString());

        if (scalarClass == TimeScalar.class) {
            return Unit.TIME;
        }
        if (scalarClass == LengthScalar.class) {
            return Unit.LENGTH;
        }

        return null;
    }

    // returns the baseunit string which is the key of the value 1.0 of the unit for the class
    public static String getBaseUnitSize(Class scalarClass) {
        return getBaseUnitSize(getUnitFromClass(scalarClass));
    }

    // returns the baseunit string which is the key of the value 1.0 of the unit
    public static String getBaseUnitSize(Unit unit) {
        for (Entry<String, Quad> ent: conversions.get(unit).entrySet()) {
            if (ent.getValue().equals(Quad.ONE)) {
                return ent.getKey();
            }
        }
        return null;
    }

    // returns all possible unitsizes which can be converted for the unit
    public static String[] getUnitSizes(Class scalarClass) {
        return getUnitSizes(getUnitFromClass(scalarClass));
    }

    // returns all possible unitsizes which can be converted for the unit
    public static String[] getUnitSizes(Unit unit) {
        Set<String> keys = conversions.get(unit).keySet();
        String[] keyStr = new String[keys.size()];
        int i = 0;
        for (String s: keys)
            keyStr[i++] = s;

        return keyStr;
    }

    // converts between different units in dimension, returns in base unit
    public static Scalar create(Quad value, Unit unit, String unitSize) throws UnitConversionError {
        Quad conversionFactor = conversions.get(unit).get(unitSize);

        return create(value.multiply(conversionFactor), unit);
    }

    // returns specific scalar class instance depending on unit
    public static Scalar create(Quad value, Unit unit) throws UnitConversionError {
        switch (unit) {
            case UNITLESS:
                return new UnitlessScalar(value);
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
            case CUBIC_MASS:
                return new CubicMassScalar(value);
            case M2_DIV_L2:
                return new M2divL2Scalar(value);
            case F_L2_Mn2:
                return new FL2Mn2Scalar(value);
            case ANGULAR_ACCELERATION:
                return new AngularAccelerationScalar(value);
            case ANGULAR_VELOCITY:
                return new AngularVelocityScalar(value);
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

    // wrapper to create with int
    public static Scalar create(int value, Unit unit, String unitSize) throws UnitConversionError {
        return ScalarFactory.create(new Quad(value), unit, unitSize);
    }

    // wrapper to create with double
    public static Scalar create(double value, Unit unit, String unitSize) throws UnitConversionError {
        return ScalarFactory.create(new Quad(value), unit, unitSize);
    }

    // wrapper to create from other scalar value
    public static Scalar create(Scalar scalar, Unit unit, String unitSize) throws UnitConversionError {
        return ScalarFactory.create(new Quad(scalar.getValue()), unit, unitSize);
    }

    // returns a gravitational constant scalar
    public static Scalar gravitationalConstant() throws UnitConversionError {
        // According to NIST Reference https://physics.nist.gov/cgi-bin/cuu/Value?bg
        return ScalarFactory.create(new Quad(6.67430).multiply(new Quad(10).pow(-11)), Unit.F_L2_Mn2);
    }
}
