package ch.hftm.astrodynamic.utils;

import java.lang.Math;

import ch.hftm.astrodynamic.scalar.AngleScalar;
import ch.hftm.astrodynamic.scalar.ScalarFactory;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */


//
public class BaseVector implements Vector, Cloneable {
    private Quad x;
    private Quad y;
    private Quad z;
    private Quad length;
    private Unit unit;

    @Override
    public boolean equals(Object arg0) {
        if (arg0 instanceof Vector) {
            Vector ov = (Vector)arg0;
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
            assert 1 == 2; // very serious error
        }
        return null;
    }

    public Scalar getY() {
        try {
            return ScalarFactory.create(y, unit);
        } catch (UnitConversionError e) {
            assert 1 == 2; // very serious error
        }
        return null;
    }

    public Scalar getZ() {
        try {
            return ScalarFactory.create(z, unit);
        } catch (UnitConversionError e) {
            assert 1 == 2; // very serious error
        }
        return null;
    }

    public Scalar getLength() {
        try {
            return ScalarFactory.create(length, unit);
        } catch (UnitConversionError e) {
            assert 1 == 2; // very serious error
        }
        return null;
    }

    private void calculateLength() {
        Quad qX = getX().getValue().pow(2);
        Quad qY = getY().getValue().pow(2);
        Quad qZ = getZ().getValue().pow(2);

        this.length = qX.add(qY).add(qZ).sqrt();
    }

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

    public Unit getUnit() {
        return unit;
    }

    public Vector add(Vector vector) throws UnitConversionError {
        return new BaseVector(getX().add(vector.getX()), getY().add(vector.getY()), getZ().add(vector.getZ()));
    }

    public Vector subtract(Vector vector) throws UnitConversionError {
        return new BaseVector(getX().subtract(vector.getX()), getY().subtract(vector.getY()), getZ().subtract(vector.getZ()));
    }

    public Vector multiply(Vector vector) throws UnitConversionError {
        return new BaseVector(getX().multiply(vector.getX()), getY().multiply(vector.getY()), getZ().multiply(vector.getZ()));
    }

    public Vector multiply(Scalar value) throws UnitConversionError {
        return new BaseVector(getX().multiply(value), getY().multiply(value), getZ().multiply(value));
    }

    public Vector divide(Vector vector) throws UnitConversionError {
        return new BaseVector(getX().divide(vector.getX()), getY().divide(vector.getY()), getZ().divide(vector.getZ()));
    }

    public Vector divide(Scalar value) throws UnitConversionError {
        return new BaseVector(getX().divide(value), getY().divide(value), getZ().divide(value));
    }

    public Vector normalize() throws UnitConversionError {
        return new BaseVector(divide(getLength()));
    }
    
    public Vector percentage() throws UnitConversionError {
    	return new BaseVector(divide(this.getX().add(this.getY()).add(this.getZ())));
    }

    public Vector rotateZ(AngleScalar rotation) throws UnitConversionError {
        Scalar sinR = new AngleScalar(Math.sin(rotation.getValue().doubleValue()));
        Scalar cosR = new AngleScalar(Math.cos(rotation.getValue().doubleValue()));
        return new BaseVector(
            this.getX().multiply(cosR).subtract(this.getY().multiply(sinR)),
            this.getX().multiply(sinR).add(this.getY().multiply(cosR)),
            this.getZ()
        );
    }

    public Vector rotateY(AngleScalar rotation) throws UnitConversionError {
        Scalar sinR = new AngleScalar(Math.sin(rotation.getValue().doubleValue()));
        Scalar cosR = new AngleScalar(Math.cos(rotation.getValue().doubleValue()));
        return new BaseVector(
            this.getX().multiply(cosR).add(this.getZ().multiply(sinR)),
            this.getY(),
            this.getX().negate().multiply(sinR).add(this.getZ().multiply(cosR))
        );
    }

    public Vector rotateX(AngleScalar rotation) throws UnitConversionError {
        Scalar sinR = new AngleScalar(Math.sin(rotation.getValue().doubleValue()));
        Scalar cosR = new AngleScalar(Math.cos(rotation.getValue().doubleValue()));
        return new BaseVector(
            this.getX(),
            this.getY().multiply(cosR).subtract(this.getZ().multiply(sinR)),
            this.getY().multiply(sinR).add(this.getZ().multiply(cosR))
        );
    }

    public String toString() {
        return String.format("<Vector %s (%s, %s, %s, %s)>", this.getClass().getSimpleName(), getX().getValue().toString(), getY().getValue().toString(), getZ().getValue().toString(), getUnit().toString());
    }
}
