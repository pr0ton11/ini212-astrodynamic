/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

package ch.hftm.astrodynamic.physics;

import ch.hftm.astrodynamic.utils.*;

// Can be a planet, moon or asteroid, shape is a sphere
public class Planetoid implements AstronomicalObject, Atmosphere {

    private Scalar zeroPointHeight;
    private Scalar mass;
    private Vector3d position;
    private Vector3d rotation;
    private Vector3d velocity;
    private Vector3d rotationalVelocity;

    public Planetoid(double zeroPointHeight, double mass, Vector3d position, Vector3d rotation, Vector3d velocity, Vector3d rotationalVelocity) {
        this.zeroPointHeight = new BaseScalar(zeroPointHeight, Unit.LENGTH);
        this.mass = new MassScalar(mass);
        this.position = position.clone();
        this.rotation = rotation.clone();
        this.velocity = velocity.clone();
        this.rotationalVelocity = rotationalVelocity.clone();
    }

    public Collision calculateCollision(AstronomicalObject partnerShape) throws UnitConversionError {
        Vector3d midpoint = getPosition().add(partnerShape.getPosition()).divide(new BaseScalar(2.0));

        if ((this.isColliding(midpoint)) && (partnerShape.isColliding(midpoint))){
            Collision collision = new Collision();
            collision.shapeA = this;
            collision.shapeB = partnerShape;
            collision.impactPointFromA = midpoint.subtract(getPosition());
            collision.impactPointFromB = midpoint.subtract(partnerShape.getPosition());
            collision.impactEnergy = calculateImpactEnergy(getVelocity(), partnerShape.getVelocity(), getMass(), partnerShape.getMass());
            return collision;
        }

        return null;
    }

    // E = 1/2 m v²
    public Scalar calculateImpactEnergy(Vector3d velocityA, Vector3d velocityB, Scalar massA, Scalar massB) throws UnitConversionError {
        return new ForceScalar(massA.add(massB).multiply(velocityA.subtract(velocityB).getLength().pow(2)).divide(new BaseScalar(2)));
    }

    public Scalar getZeroElevation() {
        return zeroPointHeight;
    }

    public Scalar getDensity(Vector3d position) {
        return new BaseScalar(0);
    }

    public Scalar getDensity(Scalar height) {
        return new BaseScalar(0);
    }

    public Scalar getOxygenPercentage() {
        return new BaseScalar(0);
    }

    public Scalar getMass() {
        return mass;
    }

    public Vector3d getPosition() {
        return position.clone();
    }

    public Vector3d getVelocity() {
        return velocity.clone();
    }

    public Vector3d getRotation() {
        return rotation.clone();
    }

    public Vector3d getRotationVelocity() {
        return rotationalVelocity.clone();
    }

    public void setMass(Scalar mass) {
        this.mass = mass;
    }

    public void setPosition(Vector3d position) {
        this.position = position.clone();
    }

    public void setVelocity(Vector3d velocity) {
        this.velocity = velocity.clone();
    }

    public void setRotation(Vector3d rotation) {
        this.rotation = rotation.clone();
    }

    public void setRotationVelocity(Vector3d rotationVelocity) {
        this.rotationalVelocity = rotationVelocity.clone();
    }

    // Planetoid is an idealized sphere
    public boolean isColliding(Vector3d offset) {
        return offset.getLength().getValue().le(getZeroElevation().getValue());
    }
    
}
