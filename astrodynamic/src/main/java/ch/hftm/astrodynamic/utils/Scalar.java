package ch.hftm.astrodynamic.utils;

public interface Scalar {
    Unit getUnit();
    Quad getValue();

    Scalar addition(Scalar scalar) throws UnitConversionError;
    Scalar subtraction(Scalar scalar) throws UnitConversionError;
    Scalar multiplication(Scalar scalar) throws UnitConversionError;
    Scalar division(Scalar scalar) throws UnitConversionError;
}
