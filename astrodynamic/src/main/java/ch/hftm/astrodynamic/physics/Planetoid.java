/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

package ch.hftm.astrodynamic.physics;

import ch.hftm.astrodynamic.utils.Vector3d;

// Can be a planet, moon or asteroid, shape is a sphere
public class Planetoid implements AstronomicalObject, Atmosphere {

    private double zeroPointHeight;
    private double mass;
    private Vector3d position;
    private Vector3d rotation;
    private Vector3d velocity;
    private Vector3d rotationalVelocity;

    public Planetoid(double zeroPointHeight, double mass, Vector3d position, Vector3d rotation, Vector3d velocity, Vector3d rotationalVelocity) {
        this.zeroPointHeight = zeroPointHeight;
        this.mass = mass;
        this.position = position.clone();
        this.rotation = rotation.clone();
        this.velocity = velocity.clone();
        this.rotationalVelocity = rotationalVelocity.clone();
    }

    public Collision calculateCollision(AstronomicalObject partnerShape) {
        /*try {
            if (Planetoid.class.isAssignableFrom(partnerShape.getClass())) {
                Planetoid partner = (Planetoid)partnerShape;
            }
        } catch (NullPointerException e) {
            System.out.println(e);
        }*/

        Vector3d midpoint = getPosition().add(partnerShape.getPosition()).divide(2);

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
    public double calculateImpactEnergy(Vector3d velocityA, Vector3d velocityB, double massA, double massB) {
        double totalVelocity = velocityA.subtract(velocityB).getLength();
        return 0.5 * (massA + massB) * (totalVelocity * totalVelocity);
    }

    public double getZeroElevation() {
        return zeroPointHeight;
    }

    public double getDensity(Vector3d position) {
        return 0;
    }

    public double getDensity(double height) {
        return 0;
    }

    public double getOxygenPercentage() {
        return 0;
    }

    public double getMass() {
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

    public void setMass(double mass) {
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
        return offset.getLength() <= getZeroElevation();
    }
    
}
