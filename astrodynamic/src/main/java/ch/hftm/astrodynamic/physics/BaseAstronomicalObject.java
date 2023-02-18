
package ch.hftm.astrodynamic.physics;

import ch.hftm.astrodynamic.scalar.AngleScalar;
import ch.hftm.astrodynamic.scalar.ForceScalar;
import ch.hftm.astrodynamic.scalar.LengthScalar;
import ch.hftm.astrodynamic.scalar.MassScalar;
import ch.hftm.astrodynamic.scalar.ScalarFactory;
import ch.hftm.astrodynamic.scalar.UnitlessScalar;
import ch.hftm.astrodynamic.scalar.VelocityScalar;

import ch.hftm.astrodynamic.utils.*;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// Base to fill computational methods, abstract to force use of specific child classes
public abstract class BaseAstronomicalObject implements AstronomicalObject, Named {
    private Scalar zeroPointHeight;
    private Scalar mass;
    private Vector position;
    private Vector rotation;
    private Vector velocity;
    private Vector rotationalVelocity;

    private String name;
    private String description;

    public BaseAstronomicalObject(double zeroPointHeight, double mass, Vector position, Vector rotation, Vector velocity, Vector rotationalVelocity) {
        this(new LengthScalar(zeroPointHeight), new MassScalar(mass), position, rotation, velocity, rotationalVelocity);
    }

    public BaseAstronomicalObject(Scalar zeroPointHeight, Scalar mass, Vector position, Vector rotation, Vector velocity, Vector rotationalVelocity) {
        this.zeroPointHeight = new LengthScalar(zeroPointHeight);
        this.mass = new MassScalar(mass);
        this.position = new BaseVector(position, Unit.LENGTH);
        this.rotation = new BaseVector(rotation, Unit.ANGLE);
        this.velocity = new BaseVector(velocity, Unit.VELOCITY);
        this.rotationalVelocity = new BaseVector(rotationalVelocity, Unit.ANGULAR_VELOCITY);
    }

    public Collision calculateCollision(AstronomicalObject partner) throws UnitConversionError {
        // our position + half the direction to the partner is the midpoint between the two bodies
        Vector midpoint = getPosition().add(getDirection(partner).divide(new UnitlessScalar(2.0)));

        // if we and the partner body both touch the midpoint we have a collision
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

    // E = 1/2 * m * v²
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

    public Vector getDirection(AstronomicalObject partner) throws UnitConversionError {
        return partner.getPosition().subtract(getPosition());
    }

    public Scalar getDistance(AstronomicalObject partner) throws UnitConversionError {
        Scalar lenScalar = getDirection(partner).getLength();
        if (lenScalar.getValue().doubleValue() < 0)
        {
            return lenScalar.negate(); // we always return a positive distance
        }
        return lenScalar;
    }

    /* Calculates the force in Newton from the gravity between two astronomical objects
     * To keep the scalar unit dimension calculations intact intermediate helper units are used
     * kg² / m² * N * m² * kg⁻² = N
     */
    public Vector calculateGravitationalForce(AstronomicalObject partner) throws UnitConversionError {
        
        // direction is calculated from partner to get direction to partner as difference (if we calculate this.pos - partner.pos we get the direction from the partner to us)
        Vector direction = getDirection(partner);
        Scalar cubic_distance = direction.getLength().multiply(direction.getLength()); // cubic distance = area

        // here we use the intermediate helper units: cubic_mass, M2_div_L2 (kg² / m²), and the gravitational constant unit F_L2_Mn2 (N * m² * kg⁻²)
        Scalar force = getMass().multiply(partner.getMass()).divide(cubic_distance).multiply(ScalarFactory.gravitationalConstant());

        direction = direction.normalize(); // make it unitless percentages for multiplication with force to get a force vector
        Vector forceVector = new BaseVector(force.multiply(direction.getX()), force.multiply(direction.getY()), force.multiply(direction.getZ()));

        return forceVector;
    }

    // a = F / m
    public Vector calculateAccelerationFromForce(Vector force) throws UnitConversionError {
        return force.divide(getMass());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Vector calculateOrbitalSpeed(AstronomicalObject partner) throws UnitConversionError {
        Scalar distance = getDistance(partner);
        Vector direction = getDirection(partner).normalize();

        // TODO: fix dimensional unit
        Scalar velocity = new VelocityScalar(partner.getMass().getValue().multiply(distance.getValue()).multiply(ScalarFactory.gravitationalConstant().getValue()));

        // here we turn the velocity vector by 90° to the partner direction to gain an orbit
        // TODO: dot/cross product would be more stable/sane
        Vector velocityVector = new BaseVector(
            velocity.multiply(direction.getX()), 
            velocity.multiply(direction.getY()), 
            velocity.multiply(direction.getZ())).rotateZ(new AngleScalar(Quad.PI.divide(Quad.TWO)));
        return velocityVector;
    }

    public void applyAcceleration(Vector velocity, Scalar time) throws UnitConversionError {
        this.velocity = this.velocity.add(velocity.multiply(time));
    }

    public void applyVelocity(Scalar time) throws UnitConversionError {
        this.position = this.position.add(this.velocity.multiply(time));
    }

    private Vector getDirectionWithRotation(AstronomicalObject partner) {
        Vector direction = new BaseVector(Unit.UNITLESS);

        try {
            direction = this.getDirection(partner);

            // TODO: order of operations?
            direction = direction.rotateX((AngleScalar)this.getRotation().getX());
            direction = direction.rotateY((AngleScalar)this.getRotation().getY());
            direction = direction.rotateZ((AngleScalar)this.getRotation().getZ());
        } catch (UnitConversionError e) {

        }

        return direction;
    }

    // returns -1.0 to 1.0 to denote the difference to the position and rotation
    public double getGrundtrackFactorX(AstronomicalObject partner) {
        Vector direction = getDirectionWithRotation(partner);
        return direction.getX().getValue().doubleValue();
    }

    // returns -1.0 to 1.0 to denote the difference to the position and rotation
    public double getGroundtrackFactorY(AstronomicalObject partner) {
        Vector direction = getDirectionWithRotation(partner);
        return direction.getY().getValue().doubleValue();
    }
}
