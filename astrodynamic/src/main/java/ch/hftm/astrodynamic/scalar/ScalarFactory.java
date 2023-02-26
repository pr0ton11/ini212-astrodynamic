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

    // milliseconds to second conversion outside of conversions for faster access
    private static long MILLIS_TO_TIME = 1000000;

    // conversions for a unittype for easier display in gui, unittype {conversionName, multiplicatorToBase}
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

        Map<String, Quad> velocityConversions = new HashMap<>();
        velocityConversions.put("m/s", new Quad(1.0));
        velocityConversions.put("km/s", new Quad(1000.0));
        velocityConversions.put("km/h", new Quad(3.6));
        conversions.put(Unit.VELOCITY, velocityConversions);

        Map<String, Quad> massConversions = new HashMap<>();
        massConversions.put("kg", new Quad(1.0));
        massConversions.put("g", new Quad(0.001));
        massConversions.put("ton", new Quad(1000.0));
        conversions.put(Unit.MASS, massConversions);
    }

    // oldValue gets converted from oldunitSize to newUnitSize
    public static Quad convert(Unit unit, Quad oldValue, String oldUnitSize, String newUnitsize) throws UnitConversionError {
        Scalar old = create(oldValue, unit, oldUnitSize);
        return convert(old, newUnitsize);
    }

    // value in standard unit gets converted to quad in unitsize 
    public static Quad convert(Scalar value, String unitsize) {
        return value.getValue().divide(getConversionFactor(value.getUnit(), unitsize));
    }

    // quad with unitsize string fitted for best display
    public static class FittedValue {
        public String unitsize;
        public Quad value;
        public Unit unit;

        FittedValue(Quad value, Unit unit, String unitsize) {
            this.unit = unit;
            this.unitsize = unitsize;
            this.value = value;
        }
    }

    // returns best fitting unit size for scalar
    public static FittedValue getFittingUnitsize(Scalar value) {
        String bestFit = getBaseUnitSize(value.getUnit());
        Quad displayValue = value.getValue();
        int displayLength = displayValue.doubleValue().toString().length();

        for (String convSize: getUnitSizes(value.getUnit())) {
            Quad comparedValue = convert(value, convSize);
            int comparedLength = comparedValue.doubleValue().toString().length();
            if (comparedLength < displayLength) {
                displayLength = comparedLength;
                displayValue = comparedValue;
                bestFit = convSize;
            }
        }

        return new FittedValue(displayValue, value.getUnit(), bestFit);
    }

    // helper gets unit for a scalar class
    public static Unit getUnitFromClass(Class scalarClass) {
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

    // searches conversion factor for unit/unitsize out of map
    private static Quad getConversionFactor(Unit unit, String unitsize) {
        return conversions.get(unit).get(unitsize);
    }

    // converts between different units in dimension, returns in base unit
    public static Scalar create(Quad value, Unit unit, String unitSize) throws UnitConversionError {
        Quad conversionFactor = getConversionFactor(unit, unitSize);

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
        return ScalarFactory.create(Quad.GRAVITATIONAL_CONSTANT, Unit.F_L2_Mn2);
    }

    // create time scalar from milliseconds
    public static Scalar createFromMillis(long value) {
        return new TimeScalar(new Quad(value / MILLIS_TO_TIME));
    }

    // create time scalar from currentTimeMillis
    public static Scalar createFromCurrentMillis() {
        return createFromMillis(System.currentTimeMillis());
    }
}
