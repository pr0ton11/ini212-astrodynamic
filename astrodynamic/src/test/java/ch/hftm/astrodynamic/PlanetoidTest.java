/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */


package ch.hftm.astrodynamic;

import org.junit.Assert;
import org.junit.Test;

import ch.hftm.astrodynamic.physics.*;
import ch.hftm.astrodynamic.scalar.AccelerationScalar;
import ch.hftm.astrodynamic.scalar.ForceScalar;
import ch.hftm.astrodynamic.scalar.LengthScalar;
import ch.hftm.astrodynamic.scalar.MassScalar;
import ch.hftm.astrodynamic.scalar.TimeScalar;
import ch.hftm.astrodynamic.utils.*;

public class PlanetoidTest {
    @Test
    public void TestImpactEnergyResting() throws UnitConversionError {
        Planetoid p1 = new Planetoid(1, 1, new BaseVector(Unit.LENGTH), new BaseVector(Unit.ANGLE), new BaseVector(Unit.VELOCITY), new BaseVector(Unit.VELOCITY));
        Planetoid p2 = new Planetoid(1, 1, new BaseVector(1, 0, 0, Unit.LENGTH), new BaseVector(Unit.ANGLE), new BaseVector(Unit.VELOCITY), new BaseVector(Unit.VELOCITY));

        Collision c1 = p1.calculateCollision(p2);
        Assert.assertEquals(0.0, c1.impactEnergy.getValue().doubleValue(), 0.0);
    }

    @Test
    public void TestImpactEnergyImpact() throws UnitConversionError {
        Planetoid p1 = new Planetoid(1, 1, new BaseVector(0, 0, 0, Unit.LENGTH), new BaseVector(Unit.ANGLE), new BaseVector(0, 0, 0, Unit.VELOCITY), new BaseVector(Unit.VELOCITY));
        Planetoid p2 = new Planetoid(1, 1, new BaseVector(1, 0, 0, Unit.LENGTH), new BaseVector(Unit.ANGLE), new BaseVector(-1, 0, 0, Unit.VELOCITY), new BaseVector(Unit.VELOCITY));

        Collision c1 = p1.calculateCollision(p2);
        // 1/2 * 2 * 1 = 1
        Assert.assertEquals(1.0, c1.impactEnergy.getValue().doubleValue(), 0.0);
    }

    @Test
    public void TestImpactEnergyVelocity() throws UnitConversionError {
        Planetoid p1 = new Planetoid(1, 1, new BaseVector(0, 0, 0, Unit.LENGTH), new BaseVector(Unit.ANGLE), new BaseVector(-5, 0, 0, Unit.VELOCITY), new BaseVector(Unit.VELOCITY));
        Planetoid p2 = new Planetoid(1, 1, new BaseVector(1, 0, 0, Unit.LENGTH), new BaseVector(Unit.ANGLE), new BaseVector(-10, 0, 0, Unit.VELOCITY), new BaseVector(Unit.VELOCITY));

        Collision c1 = p1.calculateCollision(p2);
        // 1/2 * 2 * 25 = 25
        Assert.assertEquals(25.0, c1.impactEnergy.getValue().doubleValue(), 0.0);
    }

    @Test
    public void TestImpactPointAngled() throws UnitConversionError {
        Planetoid p1 = new Planetoid(1, 1, new BaseVector(0, 0, 0, Unit.LENGTH), new BaseVector(Unit.ANGLE), new BaseVector(1, 1, 1, Unit.VELOCITY), new BaseVector(Unit.VELOCITY));
        Planetoid p2 = new Planetoid(1, 1, new BaseVector(1, 1, 1, Unit.LENGTH), new BaseVector(Unit.ANGLE), new BaseVector(-1, -1, -1, Unit.VELOCITY), new BaseVector(Unit.VELOCITY));

        Collision c1 = p1.calculateCollision(p2);

        Assert.assertEquals(0.5, c1.impactPointFromA.getX().getValue().doubleValue(), 0.0);
        Assert.assertEquals(0.5, c1.impactPointFromA.getY().getValue().doubleValue(), 0.0);
        Assert.assertEquals(0.5, c1.impactPointFromA.getZ().getValue().doubleValue(), 0.0);

        Assert.assertEquals(-0.5, c1.impactPointFromB.getX().getValue().doubleValue(), 0.0);
        Assert.assertEquals(-0.5, c1.impactPointFromB.getY().getValue().doubleValue(), 0.0);
        Assert.assertEquals(-0.5, c1.impactPointFromB.getZ().getValue().doubleValue(), 0.0);
    }

    @Test
    public void TestEarthMoonGravitation() throws UnitConversionError {
        Planetoid earth = new Planetoid(new LengthScalar(0), new MassScalar(new Quad(5.96).multiply(new Quad(10).pow(24))), new BaseVector(Unit.LENGTH), new BaseVector(Unit.ANGLE), new BaseVector(Unit.VELOCITY), new BaseVector(Unit.VELOCITY));
        Planetoid moon = new Planetoid(new LengthScalar(0), new MassScalar(new Quad(7.33).multiply(new Quad(10).pow(22))), new BaseVector(new Quad(3.84).multiply(new Quad(10).pow(8)), new Quad(), new Quad(), Unit.LENGTH), new BaseVector(Unit.ANGLE), new BaseVector(Unit.VELOCITY), new BaseVector(Unit.VELOCITY));
    
        Quad referenceForce = new Quad(1.977395353).multiply(new Quad(10).pow(20));
        Quad delta = new Quad(1).multiply(new Quad(10).pow(11));

        Vector gravitationalForce = earth.calculateGravitationalForce(moon);

        Assert.assertEquals(referenceForce.doubleValue(), gravitationalForce.getLength().getValue().doubleValue(), delta.doubleValue());
    }

    @Test
    public void TestGravityDirection() throws UnitConversionError {
        Planetoid p1 = new Planetoid(0, 1000, new BaseVector(Unit.LENGTH), new BaseVector(Unit.ANGLE), new BaseVector(Unit.VELOCITY), new BaseVector(Unit.ANGULAR_VELOCITY));
        Planetoid p2 = new Planetoid(0, 1000, new BaseVector(1000, 0, 0, Unit.LENGTH), new BaseVector(Unit.ANGLE), new BaseVector(Unit.VELOCITY), new BaseVector(Unit.ANGULAR_VELOCITY));

        Vector vExpectedDirection = new BaseVector(1, 0, 0, Unit.UNITLESS);

        Vector gravitationalForce = p1.calculateGravitationalForce(p2);

        Assert.assertEquals(vExpectedDirection, gravitationalForce.normalize());
    }

    @Test
    public void TestGravityAcceleration() throws UnitConversionError {
        LengthScalar zeroPoint = new LengthScalar(new Quad(6.374, 6));
        MassScalar earthMass = new MassScalar(new Quad(5.9722, 24));
        Planetoid earth = new Planetoid(zeroPoint, earthMass, new BaseVector(Unit.LENGTH), new BaseVector(Unit.ANGLE), new BaseVector(Unit.VELOCITY), new BaseVector(Unit.VELOCITY));
        Planetoid person = new Planetoid(0, 80, new BaseVector(zeroPoint.getValue(), new Quad(), new Quad(), Unit.LENGTH), new BaseVector(Unit.ANGLE), new BaseVector(Unit.VELOCITY), new BaseVector(Unit.ANGULAR_VELOCITY));

        Vector vExpectedDirection = new BaseVector(-9.81, 0, 0, Unit.ACCELERATION);

        Vector gravitationalForce = person.calculateGravitationalForce(earth);
        Vector gravitationalAcceleration = person.calculateAccelerationFromForce(gravitationalForce);

        Assert.assertTrue(vExpectedDirection.getLength().almostEquals(gravitationalAcceleration.getLength(), new Quad(0.01)));
    }

    // the famous apple should have an acceleration of 9.81 m/s², it is derived from the example on this website https://www.mathsisfun.com/physics/gravity.html 
    @Test
    public void TestAppleOnEarth() throws UnitConversionError {
        ForceScalar expectedForce = new ForceScalar(new Quad(0.98));
        Quad expectedForceAcceptableError = new Quad(0.01);

        AccelerationScalar expectedAccelerationApple = new AccelerationScalar(new Quad(9.81));
        Quad expectedAccelerationAppleAcceptableError = new Quad(0.01);
        AccelerationScalar expectedAccelerationEarth = new AccelerationScalar(new Quad(1.64,-25));
        Quad expectedAccelerationEarthAcceptableError = new Quad(1, -25);

        Planetoid earth = new Planetoid(
            new LengthScalar(), // zero elevation should not matter for this example
            new MassScalar(new Quad(5.972, 24)), 
            new BaseVector(Unit.LENGTH), // possitioned at coordinate origin 
            new BaseVector(Unit.ANGLE), 
            new BaseVector(Unit.VELOCITY), 
            new BaseVector(Unit.ANGULAR_VELOCITY)
        );

        Planetoid apple = new Planetoid(
            new LengthScalar(), // zero elevation should not matter for this example
            new MassScalar(new Quad(0.1, 0)), // the apple weights 100g
            new BaseVector(new Quad(6.371, 6), Quad.ZERO, Quad.ZERO, Unit.LENGTH), // possitioned at sea level relative to earth
            new BaseVector(Unit.ANGLE), 
            new BaseVector(Unit.VELOCITY), 
            new BaseVector(Unit.ANGULAR_VELOCITY)
        );

        Vector gravitationalForce = apple.calculateGravitationalForce(earth);

        Assert.assertTrue(
            String.format("Earth/Apple gravitational force expected %s, measured %s", expectedForce.toString(), gravitationalForce.getLength().toString()),
            gravitationalForce.getLength().almostEquals(expectedForce, expectedForceAcceptableError)
        );

        Vector appleAcceleration = apple.calculateAccelerationFromForce(gravitationalForce);
        Vector earthAcceleration = earth.calculateAccelerationFromForce(gravitationalForce.invert());

        Assert.assertTrue(
            String.format("Apple gravitational acceleration expected %s, measured %s", expectedAccelerationApple.toString(), appleAcceleration.getLength().toString()),
            appleAcceleration.getLength().almostEquals(expectedAccelerationApple, expectedAccelerationAppleAcceptableError)
        );

        Assert.assertTrue(
            String.format("Earth gravitational acceleration expected %s, measured %s", expectedAccelerationEarth.toString(), earthAcceleration.getLength().toString()),
            appleAcceleration.getLength().almostEquals(expectedAccelerationEarth, expectedAccelerationEarthAcceptableError)
        );

        // now we apply 1 second of this acceleration and check if the apple ends up where we expect it to
        Vector expectedPosition = apple.getPosition().subtract(new BaseVector(appleAcceleration.getLength().getValue(), Quad.ZERO, Quad.ZERO, Unit.LENGTH));
        Quad expectedPositionAcceptableError = Quad.ONE;

        TimeScalar oneSecond = new TimeScalar(Quad.ONE);
        apple.applyAcceleration(appleAcceleration, oneSecond);
        apple.applyVelocity(oneSecond);

        Assert.assertTrue(
            String.format("Apple position expected to be %s from center, measured %s", expectedPosition.getLength().toString(), apple.getPosition().getLength().toString()),
            apple.getPosition().getLength().almostEquals(expectedPosition.getLength(), expectedPositionAcceptableError)
        );
    }

    @Test
    public void TestEarthMoonGravitationAcceleration() throws UnitConversionError {
        Planetoid earth = new Planetoid(new LengthScalar(0), new MassScalar(new Quad(5.96).multiply(new Quad(10).pow(24))), new BaseVector(Unit.LENGTH), new BaseVector(Unit.ANGLE), new BaseVector(Unit.VELOCITY), new BaseVector(Unit.VELOCITY));
        Planetoid moon = new Planetoid(new LengthScalar(0), new MassScalar(new Quad(7.33).multiply(new Quad(10).pow(22))), new BaseVector(new Quad(3.84).multiply(new Quad(10).pow(8)), new Quad(), new Quad(), Unit.LENGTH), new BaseVector(Unit.ANGLE), new BaseVector(Unit.VELOCITY), new BaseVector(Unit.VELOCITY));

        Vector gravitationalForce = moon.calculateGravitationalForce(earth);
        Vector gravitationalAcceleration = moon.calculateAccelerationFromForce(gravitationalForce);

    }
}
