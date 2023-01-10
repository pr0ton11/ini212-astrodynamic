package ch.hftm.astrodynamic.utils;

public interface Scalar {
    SIUnit getUnit();
    Quad getValue();

    Scalar addition(Scalar scalar) throws SIConversionError;
    Scalar subtraction(Scalar scalar) throws SIConversionError;
    Scalar multiplication(Scalar scalar) throws SIConversionError;
    Scalar division(Scalar scalar) throws SIConversionError;
}
