/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

package ch.hftm.astrodynamic.utils;

import java.lang.Math;

public class Vector3d implements Cloneable {
    private Quad x;
    private Quad y;
    private Quad z;
    private Quad length;
    private Unit unit;

    public boolean equals(Object o) {
        if (o instanceof Vector3d) {
            Vector3d ov = (Vector3d)o;
            return ((ov.getX() == this.getX()) && (ov.getY() == this.getY()) && (ov.getZ() == this.getZ()));
        }
        return false;
    }

    public Vector3d clone() {
        return new Vector3d(this);
    }

    public Scalar getX() {
        return new BaseScalar(x, unit);
    }

    public Scalar getY() {
        return new BaseScalar(y, unit);
    }

    public Scalar getZ() {
        return new BaseScalar(z, unit);
    }

    public Scalar getLength() {
        return new BaseScalar(length, unit);
    }

    private void calculateLength() {
        Quad qX = getX().getValue().pow(2);
        Quad qY = getY().getValue().pow(2);
        Quad qZ = getZ().getValue().pow(2);

        this.length = qX.add(qY).add(qZ).sqrt();
    }

    public Vector3d() {
        this.x = new Quad();
        this.y = new Quad();
        this.z = new Quad();
        this.unit = Unit.UNITLESS;
        calculateLength();
    }

    public Vector3d(double x, double y, double z, Unit unit) {
        this.x = new Quad(x);
        this.y = new Quad(y);
        this.z = new Quad(z);
        this.unit = unit;
        calculateLength();
    }

    public Vector3d(Quad x, Quad y, Quad z, Unit unit) {
        this.x = new Quad(x);
        this.y = new Quad(y);
        this.z = new Quad(z);
        this.unit = unit;
        calculateLength();
    }

    public Vector3d(Scalar x, Scalar y, Scalar z) throws UnitConversionError {

        if ((x.getUnit() != y.getUnit()) || (x.getUnit() != z.getUnit())) {
            throw new UnitConversionError("All scalar must have same unit");
        }

        this.x = x.getValue();
        this.y = y.getValue();
        this.z = z.getValue();
        this.unit = x.getUnit();
        calculateLength();
    }

    public Vector3d(Vector3d vector) {
        this.x = vector.getX().getValue();
        this.y = vector.getY().getValue();
        this.z = vector.getZ().getValue();
        this.unit = vector.getUnit();
        calculateLength();
    }

    public Unit getUnit() {
        return unit;
    }

    public Vector3d add(Vector3d vector) throws UnitConversionError {
        return new Vector3d(getX().add(vector.getX()), getY().add(vector.getY()), getZ().add(vector.getZ()));
    }

    public Vector3d subtract(Vector3d vector) throws UnitConversionError {
        return new Vector3d(getX().subtract(vector.getX()), getY().subtract(vector.getY()), getZ().subtract(vector.getZ()));
    }

    public Vector3d multiply(Vector3d vector) throws UnitConversionError {
        return new Vector3d(getX().multiply(vector.getX()), getY().multiply(vector.getY()), getZ().multiply(vector.getZ()));
    }

    public Vector3d multiplication(Scalar value) throws UnitConversionError {
        return new Vector3d(getX().multiply(value), getY().multiply(value), getZ().multiply(value));
    }

    public Vector3d multiplication(double value) throws UnitConversionError {
        return multiplication(new BaseScalar(value));
    }

    public Vector3d divide(Vector3d vector) throws UnitConversionError {
        return new Vector3d(getX().divide(vector.getX()), getY().divide(vector.getY()), getZ().divide(vector.getZ()));
    }

    public Vector3d divide(Scalar value) throws UnitConversionError {
        return new Vector3d(getX().divide(value), getY().divide(value), getZ().divide(value));
    }

    public Vector3d divide(double value) throws UnitConversionError {
        return divide(new BaseScalar(value));
    }

    public Vector3d normalize() throws UnitConversionError {
        return new Vector3d(divide(getLength()));
    }
    
    public Vector3d percentage() throws UnitConversionError {
    	return new Vector3d(divide(this.getX().add(this.getY()).add(this.getZ())));
    }

    public Vector3d rotateZ(double rotationRadians) throws UnitConversionError {
        Scalar sinR = new BaseScalar(Math.sin(rotationRadians));
        Scalar cosR = new BaseScalar(Math.cos(rotationRadians));
        return new Vector3d(
            this.getX().multiply(cosR).subtract(this.getY().multiply(sinR)),
            this.getX().multiply(sinR).add(this.getY().multiply(cosR)),
            this.getZ()
        );
    }

    public Vector3d rotateY(double rotationRadians) throws UnitConversionError {
        Scalar sinR = new BaseScalar(Math.sin(rotationRadians));
        Scalar cosR = new BaseScalar(Math.cos(rotationRadians));
        return new Vector3d(
            this.getX().multiply(cosR).add(this.getZ().multiply(sinR)),
            this.getY(),
            this.getX().negate().multiply(sinR).add(this.getZ().multiply(cosR))
        );
    }

    public Vector3d rotateX(double rotationRadians) throws UnitConversionError {
        Scalar sinR = new BaseScalar(Math.sin(rotationRadians));
        Scalar cosR = new BaseScalar(Math.cos(rotationRadians));
        return new Vector3d(
            this.getX(),
            this.getY().multiply(cosR).subtract(this.getZ().multiply(sinR)),
            this.getY().multiply(sinR).add(this.getZ().multiply(cosR))
        );
    }

    public String toString() {
        return String.format("Vec3 (%f, %f, %f, %s)", getX().getValue().doubleValue(), getY().getValue().doubleValue(), getZ().getValue().doubleValue(), getUnit().toString());
    }
}
