package ch.hftm.astrodynamic.utils;

import java.io.Serializable;

import ch.hftm.astrodynamic.scalar.AngleScalar;
import ch.hftm.astrodynamic.scalar.UnitlessScalar;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

 // Vector interface, see BaseVector for implementation
public interface Vector {

    Scalar getX();

    Scalar getY();

    Scalar getZ();

    Scalar getLength();

    Unit getUnit();

    Vector add(Vector vector) throws UnitConversionError;

    Vector subtract(Vector vector) throws UnitConversionError;

    Vector multiply(Scalar value) throws UnitConversionError;

    Vector divide(Scalar value) throws UnitConversionError;

    Vector normalize() throws UnitConversionError;

    Vector rotateZ(AngleScalar rotation) throws UnitConversionError;

    Vector rotateY(AngleScalar rotation) throws UnitConversionError;

    Vector rotateX(AngleScalar rotation) throws UnitConversionError;

    // rotate on z axis, default implementation to allow double as parameter
    default Vector rotateZ(double rotationRadians) throws UnitConversionError {
        return this.rotateZ(new AngleScalar(rotationRadians));
    }

    // rotate on y axis, default implementation to allow double as parameter
    default Vector rotateY(double rotationRadians) throws UnitConversionError {
        return this.rotateY(new AngleScalar(rotationRadians));
    }

    // rotate on x axis, default implementation to allow double as parameter
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
