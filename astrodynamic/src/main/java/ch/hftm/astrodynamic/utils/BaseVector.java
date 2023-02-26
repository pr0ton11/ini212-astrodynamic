package ch.hftm.astrodynamic.utils;

import java.util.logging.Logger;

import java.lang.Math;

import ch.hftm.astrodynamic.scalar.AngleScalar;
import ch.hftm.astrodynamic.scalar.ScalarFactory;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

 // implements basic functionality for vectors
public class BaseVector implements Vector, Cloneable {
    private Quad x;
    private Quad y;
    private Quad z;
    private Quad length;
    private Unit unit;

    private static Logger logger = Log.build();

    // unitless zero vector
    public BaseVector() {
        this.x = new Quad();
        this.y = new Quad();
        this.z = new Quad();
        this.unit = Unit.UNITLESS;
        calculateLength();
    }

    // unit zero vector
    public BaseVector(Unit unit) {
        this.x = new Quad();
        this.y = new Quad();
        this.z = new Quad();
        this.unit = unit;
        calculateLength();
    }

    public BaseVector(double x, double y, double z, Unit unit) {
        this.x = new Quad(x);
        this.y = new Quad(y);
        this.z = new Quad(z);
        this.unit = unit;
        calculateLength();
    }

    public BaseVector(Quad x, Quad y, Quad z, Unit unit) {
        this.x = new Quad(x);
        this.y = new Quad(y);
        this.z = new Quad(z);
        this.unit = unit;
        calculateLength();
    }

    public BaseVector(double x, double y, double z) {
        this.x = new Quad(x);
        this.y = new Quad(y);
        this.z = new Quad(z);
        this.unit = Unit.UNITLESS;
        calculateLength();
    }

    public BaseVector(Quad x, Quad y, Quad z) {
        this.x = new Quad(x);
        this.y = new Quad(y);
        this.z = new Quad(z);
        this.unit = Unit.UNITLESS;
        calculateLength();
    }

    public BaseVector(Scalar x, Scalar y, Scalar z) throws UnitConversionError {

        if ((x.getUnit() != y.getUnit()) || (x.getUnit() != z.getUnit())) {
            throw new UnitConversionError("All scalar must have same unit");
        }

        this.x = x.getValue();
        this.y = y.getValue();
        this.z = z.getValue();
        this.unit = x.getUnit();
        calculateLength();
    }

    public BaseVector(Vector vector) {
        this.x = vector.getX().getValue();
        this.y = vector.getY().getValue();
        this.z = vector.getZ().getValue();
        this.unit = vector.getUnit();
        calculateLength();
    }

    public BaseVector(Vector vector, Unit unit) {
        this.x = vector.getX().getValue();
        this.y = vector.getY().getValue();
        this.z = vector.getZ().getValue();
        this.unit = unit;
        calculateLength();
    }

    @Override
    public boolean equals(Object arg0) {
        if (arg0 instanceof Vector) {
            Vector ov = (Vector)arg0;
            // vecotrs are equal if all three dimensions (x, y, z) are equal and the unit is equal
            return ((ov.getX().equals(this.getX())) && (ov.getY().equals(this.getY())) && (ov.getZ().equals(this.getZ())) && (ov.getUnit() == this.getUnit()));
        }
        return false;
    }

    public BaseVector clone() {
        return new BaseVector(this);
    }

    public Scalar getX() {
        try {
            return ScalarFactory.create(x, unit);
        } catch (UnitConversionError e) {
            logger.severe(String.format("Could not get scalar from %s .", this.toString()));
        }
        return null;
    }

    public Scalar getY() {
        try {
            return ScalarFactory.create(y, unit);
        } catch (UnitConversionError e) {
            logger.severe(String.format("Could not get scalar from %s .", this.toString()));
        }
        return null;
    }

    public Scalar getZ() {
        try {
            return ScalarFactory.create(z, unit);
        } catch (UnitConversionError e) {
            logger.severe(String.format("Could not get scalar from %s .", this.toString()));
        }
        return null;
    }

    public Scalar getLength() {
        try {
            return ScalarFactory.create(length, unit);
        } catch (UnitConversionError e) {
            logger.severe(String.format("Could not get scalar from %s .", this.toString()));
        }
        return null;
    }

    // magnitude from taking the root of ( x² + y² + z² )
    private void calculateLength() {
        Quad qX = getX().getValue().pow(2);
        Quad qY = getY().getValue().pow(2);
        Quad qZ = getZ().getValue().pow(2);

        this.length = qX.add(qY).add(qZ).sqrt();
    }

    public Unit getUnit() {
        return unit;
    }

    // addition (x1 + x2, y1 + y2, z1 + z2)
    public Vector add(Vector vector) throws UnitConversionError {
        return new BaseVector(getX().add(vector.getX()), getY().add(vector.getY()), getZ().add(vector.getZ()));
    }

    // subtraction (x1 - x2, y1 - y2, z1 - z2)
    public Vector subtract(Vector vector) throws UnitConversionError {
        return new BaseVector(getX().subtract(vector.getX()), getY().subtract(vector.getY()), getZ().subtract(vector.getZ()));
    }

    // multiplication with a scalar (x * s, y * s, z * s), scales the vector magnitude, direction stays the same
    public Vector multiply(Scalar value) throws UnitConversionError {
        return new BaseVector(getX().multiply(value), getY().multiply(value), getZ().multiply(value));
    }

    // division with a scalar (x / s, y / s, z / s), scales the vector magnitude, direction stays the same
    public Vector divide(Scalar value) throws UnitConversionError {
        return new BaseVector(getX().divide(value), getY().divide(value), getZ().divide(value));
    }

    // make a unit vector with magnitude (length) 1.0 out of this, direction stays the same
    public Vector normalize() throws UnitConversionError {
        return new BaseVector(divide(getLength()));
    }

    // roatate the vector around the z axis
    public Vector rotateZ(AngleScalar rotation) throws UnitConversionError {
        Scalar sinR = new AngleScalar(Math.sin(rotation.getValue().doubleValue()));
        Scalar cosR = new AngleScalar(Math.cos(rotation.getValue().doubleValue()));
        return new BaseVector(
            this.getX().multiply(cosR).subtract(this.getY().multiply(sinR)),
            this.getX().multiply(sinR).add(this.getY().multiply(cosR)),
            this.getZ()
        );
    }

    // rotate the vector around the y axis
    public Vector rotateY(AngleScalar rotation) throws UnitConversionError {
        Scalar sinR = new AngleScalar(Math.sin(rotation.getValue().doubleValue()));
        Scalar cosR = new AngleScalar(Math.cos(rotation.getValue().doubleValue()));
        return new BaseVector(
            this.getX().multiply(cosR).add(this.getZ().multiply(sinR)),
            this.getY(),
            this.getX().negate().multiply(sinR).add(this.getZ().multiply(cosR))
        );
    }

    // rotate the vector around the x axis
    public Vector rotateX(AngleScalar rotation) throws UnitConversionError {
        Scalar sinR = new AngleScalar(Math.sin(rotation.getValue().doubleValue()));
        Scalar cosR = new AngleScalar(Math.cos(rotation.getValue().doubleValue()));
        return new BaseVector(
            this.getX(),
            this.getY().multiply(cosR).subtract(this.getZ().multiply(sinR)),
            this.getY().multiply(sinR).add(this.getZ().multiply(cosR))
        );
    }

    // human readable string with class name, dimensions and unit
    public String toString() {
        return String.format("<Vector %s (%s, %s, %s, %s)>", this.getClass().getSimpleName(), getX().getValue().toString(), getY().getValue().toString(), getZ().getValue().toString(), getUnit().toString());
    }
}
