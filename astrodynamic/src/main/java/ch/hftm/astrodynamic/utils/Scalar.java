package ch.hftm.astrodynamic.utils;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

public interface Scalar {
    Unit getUnit();
    Quad getValue();

    // Math operations
    Scalar add(Scalar scalar) throws UnitConversionError;
    Scalar subtract(Scalar scalar) throws UnitConversionError;
    Scalar multiply(Scalar scalar) throws UnitConversionError;
    Scalar divide(Scalar scalar) throws UnitConversionError;
    Scalar negate();
    Scalar pow(int exp);

    // Flags
    boolean isUnitless();

    // Mathematical comperators
    public boolean gt(Scalar comperator);
    public boolean ge(Scalar comperator);
    public boolean lt(Scalar comperator);
    public boolean le(Scalar comperator);
}
