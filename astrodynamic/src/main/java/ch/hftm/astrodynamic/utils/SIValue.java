package ch.hftm.astrodynamic.utils;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

 // Converter for SI units
public class SIValue implements Scalar {

    private Quad value;  // Number to be stored in this object
    private SIUnit unit; // Unit to be stored in this object

    // Constructor for doubles
    public SIValue(Quad value, SIUnit unit) {
        this.value = new Quad(value);
        this.unit = unit;
    }

    // Constructor for int
    public SIValue(int value, SIUnit unit) {
        this.value = new Quad(value);
        this.unit = unit;
    }

    // Constructor for double
    public SIValue(double value, SIUnit unit) {
        this.value = new Quad(value);
        this.unit = unit;
    }

    // Getter for SI unit
    public SIUnit getUnit() { return this.unit; }

    @Override
    public Quad getValue() {
        return value;
    }

    @Override
    public Scalar addition(Scalar scalar) throws SIConversionError {
        // unitless: default own unit
        SIUnit targetUnit = getUnit();

        // if we are unitless ignore
        if ((scalar.getUnit() != getUnit())) {
            throw new SIConversionError(String.format("Addition between %s and %s not possible", getUnit().toString(), scalar.getUnit().toString()));
        }
        return new SIValue(getValue().add(scalar.getValue()), targetUnit);
    }

    @Override
    public Scalar subtraction(Scalar scalar) throws SIConversionError {
        // unitless: default own unit
        SIUnit targetUnit = getUnit();

        // if we are unitless ignore
        if ((scalar.getUnit() != getUnit())) {
            throw new SIConversionError(String.format("Subtraction between %s and %s not possible", getUnit().toString(), scalar.getUnit().toString()));
        }
        return new SIValue(getValue().subtract(scalar.getValue()), targetUnit);
    }

    @Override
    public Scalar multiplication(Scalar scalar) throws SIConversionError  {
        // unitless: default own unit
        SIUnit targetUnit = getUnit();
        if (targetUnit == SIUnit.UNITLESS) {
            targetUnit = scalar.getUnit();
        }

        // if we are unitless ignore
        if ((scalar.getUnit() != SIUnit.UNITLESS) && (getUnit() != SIUnit.UNITLESS) && (scalar.getUnit() != getUnit())) {
            throw new SIConversionError(String.format("Multiplication between %s and %s not possible", getUnit().toString(), scalar.getUnit().toString()));
        }
        return new SIValue(getValue().multiply(scalar.getValue()), targetUnit);
    }

    @Override
    public Scalar division(Scalar scalar) throws SIConversionError  {
        // unitless: default own unit
        SIUnit targetUnit = getUnit();
        if (targetUnit == SIUnit.UNITLESS) {
            targetUnit = scalar.getUnit();
        }

        // if we are unitless ignore
        if ((scalar.getUnit() != SIUnit.UNITLESS) && (getUnit() != SIUnit.UNITLESS) && (scalar.getUnit() != getUnit())) {
            throw new SIConversionError(String.format("Division between %s and %s not possible", getUnit().toString(), scalar.getUnit().toString()));
        }
        return new SIValue(getValue().divide(scalar.getValue()), targetUnit);
    }

}
