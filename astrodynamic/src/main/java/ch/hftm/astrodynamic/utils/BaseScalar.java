package ch.hftm.astrodynamic.utils;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

 // Converter for SI units
public class BaseScalar implements Scalar {

    private Quad value;  // Number to be stored in this object
    private Unit unit; // Unit to be stored in this object

    // Constructor for doubles
    public BaseScalar(Quad value, Unit unit) {
        this.value = new Quad(value);
        this.unit = unit;
    }

    // Constructor for int
    public BaseScalar(int value, Unit unit) {
        this.value = new Quad(value);
        this.unit = unit;
    }

    // Constructor for double
    public BaseScalar(double value, Unit unit) {
        this.value = new Quad(value);
        this.unit = unit;
    }

    // Getter for SI unit
    public Unit getUnit() { return this.unit; }

    public Quad getValue() {
        return value;
    }

    public Scalar addition(Scalar scalar) throws UnitConversionError {
        // unitless: default own unit
        Unit targetUnit = getUnit();

        // if we are unitless ignore
        if ((scalar.getUnit() != getUnit())) {
            throw new UnitConversionError(String.format("Addition between %s and %s not possible", getUnit().toString(), scalar.getUnit().toString()));
        }
        return new BaseScalar(getValue().add(scalar.getValue()), targetUnit);
    }

    public Scalar subtraction(Scalar scalar) throws UnitConversionError {
        // unitless: default own unit
        Unit targetUnit = getUnit();

        // if we are unitless ignore
        if ((scalar.getUnit() != getUnit())) {
            throw new UnitConversionError(String.format("Subtraction between %s and %s not possible", getUnit().toString(), scalar.getUnit().toString()));
        }
        return new BaseScalar(getValue().subtract(scalar.getValue()), targetUnit);
    }

    public Scalar multiplication(Scalar scalar) throws UnitConversionError  {
        // unitless: default own unit
        Unit targetUnit = getUnit();
        if (targetUnit == Unit.UNITLESS) {
            targetUnit = scalar.getUnit();
        }

        // if we are unitless ignore
        if ((scalar.getUnit() != Unit.UNITLESS) && (getUnit() != Unit.UNITLESS) && (scalar.getUnit() != getUnit())) {
            throw new UnitConversionError(String.format("Multiplication between %s and %s not possible", getUnit().toString(), scalar.getUnit().toString()));
        }
        return new BaseScalar(getValue().multiply(scalar.getValue()), targetUnit);
    }

    public Scalar division(Scalar scalar) throws UnitConversionError  {
        // unitless: default own unit
        Unit targetUnit = getUnit();
        if (targetUnit == Unit.UNITLESS) {
            targetUnit = scalar.getUnit();
        }

        // if we are unitless ignore
        if ((scalar.getUnit() != Unit.UNITLESS) && (getUnit() != Unit.UNITLESS) && (scalar.getUnit() != getUnit())) {
            throw new UnitConversionError(String.format("Division between %s and %s not possible", getUnit().toString(), scalar.getUnit().toString()));
        }
        return new BaseScalar(getValue().divide(scalar.getValue()), targetUnit);
    }

}
