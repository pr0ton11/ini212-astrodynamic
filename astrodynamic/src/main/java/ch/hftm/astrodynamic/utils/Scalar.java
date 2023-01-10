package ch.hftm.astrodynamic.utils;

public interface Scalar {
    Unit getUnit();
    Quad getValue();

    Scalar add(Scalar scalar) throws UnitConversionError;
    Scalar subtract(Scalar scalar) throws UnitConversionError;
    Scalar multiply(Scalar scalar) throws UnitConversionError;
    Scalar divide(Scalar scalar) throws UnitConversionError;
    Scalar negate();
    Scalar pow(int exp);
}
