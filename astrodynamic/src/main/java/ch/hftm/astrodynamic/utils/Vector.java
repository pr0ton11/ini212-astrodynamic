package ch.hftm.astrodynamic.utils;

import ch.hftm.astrodynamic.scalar.AngleScalar;
import ch.hftm.astrodynamic.scalar.UnitlessScalar;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

public interface Vector {

    Scalar getX();

    Scalar getY();

    Scalar getZ();

    Scalar getLength();

    Unit getUnit();

    Vector add(Vector vector) throws UnitConversionError;

    Vector subtract(Vector vector) throws UnitConversionError;

    Vector multiply(Vector vector) throws UnitConversionError;

    Vector multiply(Scalar value) throws UnitConversionError;

    Vector divide(Vector vector) throws UnitConversionError;

    Vector divide(Scalar value) throws UnitConversionError;

    Vector normalize() throws UnitConversionError;
    
    Vector percentage() throws UnitConversionError;

    Vector rotateZ(AngleScalar rotation) throws UnitConversionError;

    Vector rotateY(AngleScalar rotation) throws UnitConversionError;

    Vector rotateX(AngleScalar rotation) throws UnitConversionError;

    default Vector rotateZ(double rotationRadians) throws UnitConversionError {
        return this.rotateZ(new AngleScalar(rotationRadians));
    }

    default Vector rotateY(double rotationRadians) throws UnitConversionError {
        return this.rotateY(new AngleScalar(rotationRadians));
    }

    default Vector rotateX(double rotationRadians) throws UnitConversionError {
        return this.rotateX(new AngleScalar(rotationRadians));
    }

    // multiply with double convert to unitless scalar
    default Vector multiply(double value) throws UnitConversionError {
        return this.multiply(new UnitlessScalar(value));
    }

    // divide with double convert to unitless scalar
    default Vector divide(double value) throws UnitConversionError {
        return this.divide(new UnitlessScalar(value));
    }

    // inversion of the vector
    default Vector invert() throws UnitConversionError {
        return this.multiply(-1.0);
    }
}
