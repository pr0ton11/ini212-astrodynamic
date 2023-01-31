/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */


package ch.hftm.astrodynamic;

import org.junit.Assert;
import org.junit.Test;

import ch.hftm.astrodynamic.physics.*;
import ch.hftm.astrodynamic.scalar.LengthScalar;
import ch.hftm.astrodynamic.scalar.MassScalar;
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
}
