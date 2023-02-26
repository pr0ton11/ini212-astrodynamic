package ch.hftm.astrodynamic.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ch.hftm.astrodynamic.scalar.ScalarFactory;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

 // Converter for SI units, base is abstract to force use of specific implementations
public abstract class BaseScalar implements Scalar {

    private Quad value;  // Number to be stored in this object
    private Unit unit; // Unit to be stored in this object

    @Override
    public boolean equals(Object arg0) {
        if (arg0 instanceof Scalar) {
            Scalar os = (Scalar)arg0;
            return ((getUnit() == os.getUnit()) && (getValue().equals(os.getValue())));
        }
        return false;
    }

    // Constructor for scalar
    public BaseScalar(Scalar scalar, Unit unit) {
        this.value = scalar.getValue();
        this.unit = unit;
    }

    // Constructor for scalar
    public BaseScalar(Unit unit) {
        this.value = new Quad();
        this.unit = unit;
    }

    // Constructor for quad
    public BaseScalar(Quad value, Unit unit) {
        this.value = new Quad(value);
        this.unit = unit;
    }

    // constructor for unitless quad
    public BaseScalar(Quad value) {
        this.value = new Quad(value);
        this.unit = Unit.UNITLESS;
    }

    // Constructor for int
    public BaseScalar(int value, Unit unit) {
        this.value = new Quad(value);
        this.unit = unit;
    }

    // constructor for unitless int
    public BaseScalar(int value) {
        this.value = new Quad(value);
        this.unit = Unit.UNITLESS;
    }

    // Constructor for double
    public BaseScalar(double value, Unit unit) {
        this.value = new Quad(value);
        this.unit = unit;
    }

    // Constructor for unitless doubles
    public BaseScalar(double value) {
        this.value = new Quad(value);
        this.unit = Unit.UNITLESS;
    }

    // Getter for SI unit
    public Unit getUnit() { return this.unit; }

    // Getter for values
    public Quad getValue() {
        return value;
    }

    // Addition helper for Scalars
    public Scalar add(Scalar scalar) throws UnitConversionError {
        // Addition is only possible between matching units
        if (unitMatches(scalar)) {
            return ScalarFactory.create(getValue().add(scalar.getValue()), this.getUnit());
        }
        // All non matching units are not possible to substract
        throw new UnitConversionError(String.format("Addition between %s and %s not possible", getUnit().toString(), scalar.getUnit().toString()));
    }

    // Substraction helper for Scalars
    public Scalar subtract(Scalar scalar) throws UnitConversionError {
        // Substraction is only possible between matching units
        if (unitMatches(scalar)) {
            return ScalarFactory.create(getValue().subtract(scalar.getValue()), this.getUnit());
        }
        // All non matching units are not possible to substract
        throw new UnitConversionError(String.format("Subtraction between %s and %s not possible", getUnit().toString(), scalar.getUnit().toString()));
    }

    // Multiplication helper for Scalars
    public Scalar multiply(Scalar scalar) throws UnitConversionError  {
        if (unitMatches(scalar)) {
            // If both units match in multiplication this can not be handled in the base scalar
            // This is handled by the specialized child classes
            if(this.isUnitless()) {
                return ScalarFactory.create(getValue().multiply(scalar.getValue()), this.getUnit());
            } else {
                throw new UnitConversionError(String.format("Multiplication between two %s not possible in generic class", getUnit().toString()));
            }
        }
        // All non matching units are special cases and can not be handled by generic class
        throw new UnitConversionError(String.format("Multiplication between %s and %s not possible in generic class", getUnit().toString(), scalar.getUnit().toString()));
    }

    // Division helper function for Scalars
    public Scalar divide(Scalar scalar) throws UnitConversionError  {
        // If both units match in division, return a unitless value
        if (unitMatches(scalar)) {
            return ScalarFactory.create(getValue().divide(scalar.getValue()), Unit.UNITLESS);
        // Divide only if divisor is unitless
        // Dividing a unitless value by a unit value is not possible is the base scalar
        // This is handled by the specialized child classes
        } else if (scalar.isUnitless()) {
            return ScalarFactory.create(getValue().divide(scalar.getValue()), this.getUnit());
        }
        // For any division that does not match the two former cases a UnitConversionError must be thrown
        throw new UnitConversionError(String.format("Division between %s and %s not possible", getUnit().toString(), scalar.getUnit().toString()));
    }

    // Function returns true if unit matches between two scalars
    protected boolean unitMatches(Scalar compareTo) {
        return this.getUnit() == compareTo.getUnit();
    }

    // Function to detemine if this object is unitless
    @JsonIgnore
    public boolean isUnitless() {
        return this.getUnit() == Unit.UNITLESS;
    }

    // Negates the current scalar
    public Scalar negate() {
        try {
            return ScalarFactory.create(value.negate(), this.getUnit());
        } catch (UnitConversionError e) {
            assert 1 == 2; // Very serious error
        }
        return null;
    }

    // Pows the current scalar
    public Scalar pow(int exp) {
        try {
            return ScalarFactory.create(value.pow(exp), this.getUnit());
        } catch (UnitConversionError e) {
            assert 1 == 2; // Very serious error
        }
        return null;
    }

    public String toString() {
        return String.format("<Scalar %s (%s, %s)>", this.getClass().getSimpleName(), getValue().toString(), getUnit().toString());
    }

    // Mathematical comperators
    public boolean gt(Scalar comperator) {
        if (!unitMatches(comperator))
            assert 1 == 2; // dont do this
        return getValue().gt(comperator.getValue());
    }
    public boolean ge(Scalar comperator) {
        if (!unitMatches(comperator))
            assert 1 == 2; // dont do this
        return getValue().ge(comperator.getValue());
    }
    public boolean lt(Scalar comperator) {
        if (!unitMatches(comperator))
            assert 1 == 2; // dont do this
        return getValue().lt(comperator.getValue());
    }
    public boolean le(Scalar comperator) {
        if (!unitMatches(comperator))
            assert 1 == 2; // dont do this
        return getValue().le(comperator.getValue());
    }

    public boolean almostEquals(Scalar comperator, Quad delta) {
        if (!unitMatches(comperator))
            assert 1 == 2; // dont do this
        return this.getValue().almostEquals(comperator.getValue(), delta);
    }
}
