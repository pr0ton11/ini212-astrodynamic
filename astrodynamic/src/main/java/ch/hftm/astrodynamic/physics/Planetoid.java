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
    private BaseVector position;
    private BaseVector rotation;
    private BaseVector velocity;
    private BaseVector rotationalVelocity;

    public Planetoid(double zeroPointHeight, double mass, BaseVector position, BaseVector rotation, BaseVector velocity, BaseVector rotationalVelocity) {
        this.zeroPointHeight = new BaseScalar(zeroPointHeight, Unit.LENGTH);
        this.mass = new MassScalar(mass);
        this.position = position.clone();
        this.rotation = rotation.clone();
        this.velocity = velocity.clone();
        this.rotationalVelocity = rotationalVelocity.clone();
    }

    public Collision calculateCollision(AstronomicalObject partnerShape) throws UnitConversionError {
        Vector midpoint = getPosition().add(partnerShape.getPosition()).divide(new BaseScalar(2.0));

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
    public Scalar calculateImpactEnergy(Vector velocityA, Vector velocityB, Scalar massA, Scalar massB) throws UnitConversionError {
        return new ForceScalar(massA.add(massB).multiply(velocityA.subtract(velocityB).getLength().pow(2)).divide(new BaseScalar(2)));
    }

    public Scalar getZeroElevation() {
        return zeroPointHeight;
    }

    public Scalar getDensity(Vector position) {
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

    public Vector getPosition() {
        return position.clone();
    }

    public Vector getVelocity() {
        return velocity.clone();
    }

    public Vector getRotation() {
        return rotation.clone();
    }

    public Vector getRotationVelocity() {
        return rotationalVelocity.clone();
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

    // Planetoid is an idealized sphere
    public boolean isColliding(Vector offset) {
        return offset.getLength().getValue().le(getZeroElevation().getValue());
    }
    
}
