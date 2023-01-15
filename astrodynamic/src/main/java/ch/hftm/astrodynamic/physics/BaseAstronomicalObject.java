/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

package ch.hftm.astrodynamic.physics;

import ch.hftm.astrodynamic.utils.*;

public class BaseAstronomicalObject implements AstronomicalObject {
    private Scalar zeroPointHeight;
    private Scalar mass;
    private Vector position;
    private Vector rotation;
    private Vector velocity;
    private Vector rotationalVelocity;

    public BaseAstronomicalObject(double zeroPointHeight, double mass, Vector position, Vector rotation, Vector velocity, Vector rotationalVelocity) {
        this.zeroPointHeight = new LengthScalar(zeroPointHeight);
        this.mass = new MassScalar(mass);
        this.position = new BaseVector(position, Unit.LENGTH);
        this.rotation = new BaseVector(rotation);
        this.velocity = new BaseVector(velocity);
        this.rotationalVelocity = new BaseVector(rotationalVelocity);
    }

    public BaseAstronomicalObject(Scalar zeroPointHeight, Scalar mass, Vector position, Vector rotation, Vector velocity, Vector rotationalVelocity) {
        this.zeroPointHeight = new LengthScalar(zeroPointHeight);
        this.mass = new MassScalar(mass);
        this.position = new BaseVector(position, Unit.LENGTH);
        this.rotation = new BaseVector(rotation);
        this.velocity = new BaseVector(velocity);
        this.rotationalVelocity = new BaseVector(rotationalVelocity);
    }

    public Collision calculateCollision(AstronomicalObject partner) throws UnitConversionError {
        Vector midpoint = getPosition().add(partner.getPosition()).divide(new BaseScalar(2.0));

        if ((this.isColliding(midpoint)) && (partner.isColliding(midpoint))){
            Collision collision = new Collision();
            collision.shapeA = this;
            collision.shapeB = partner;
            collision.impactPointFromA = midpoint.subtract(getPosition());
            collision.impactPointFromB = midpoint.subtract(partner.getPosition());
            collision.impactEnergy = calculateImpactEnergy(getVelocity(), partner.getVelocity(), getMass(), partner.getMass());
            return collision;
        }

        return null;
    }

    // E = 1/2 m v²
    public Scalar calculateImpactEnergy(Vector velocityA, Vector velocityB, Scalar massA, Scalar massB) throws UnitConversionError {
        return new ForceScalar(massA.add(massB).multiply(velocityA.subtract(velocityB).getLength().pow(2)).divide(new UnitlessScalar(2)));
    }

    public Scalar getZeroElevation() {
        return zeroPointHeight;
    }

    public Scalar getMass() {
        return mass;
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public Vector getRotation() {
        return rotation;
    }

    public Vector getRotationVelocity() {
        return rotationalVelocity;
    }

    public void setMass(Scalar mass) {
        this.mass = mass;
    }

    public void setPosition(Vector position) {
        this.position = new BaseVector(position);
    }

    public void setVelocity(Vector velocity) {
        this.velocity = new BaseVector(velocity);
    }

    public void setRotation(Vector rotation) {
        this.rotation = new BaseVector(rotation);
    }

    public void setRotationVelocity(Vector rotationVelocity) {
        this.rotationalVelocity = new BaseVector(rotationVelocity);
    }

    // Standard an idealized sphere
    public boolean isColliding(Vector offset) {
        return offset.getLength().getValue().le(getZeroElevation().getValue());
    }

    /* Calculates the force in Newton from the gravity between two astronomical objects
     * To keep the scalar unit dimension calculations intact intermediate helper units are used
     * kg² / m² * N * m² * kg⁻² = N
     */
    public Vector calculateGravitationalForce(AstronomicalObject partner) throws UnitConversionError {
        
        Vector direction = getPosition().subtract(partner.getPosition());
        Scalar cubic_distance = direction.getLength().multiply(direction.getLength()); // cubic distance = area

        // here we use the intermediate helper units: cubic_mass, M2_div_L2 (kg² / m²), and the gravitational constant unit F_L2_Mn2 (N * m² * kg⁻²)
        Scalar force = getMass().multiply(partner.getMass()).divide(cubic_distance).multiply(ScalarFactory.gravitationalConstant());

        direction = direction.normalize();

        Vector forceVector = new BaseVector(force.multiply(direction.getX()), force.multiply(direction.getY()), force.multiply(direction.getZ()));

        return forceVector;
    }
}