/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */


package ch.hftm.astrodynamic;

import org.junit.Assert;
import org.junit.Test;

import ch.hftm.astrodynamic.physics.Planetoid;
import ch.hftm.astrodynamic.physics.Collision;
import ch.hftm.astrodynamic.utils.Unit;
import ch.hftm.astrodynamic.utils.UnitConversionError;
import ch.hftm.astrodynamic.utils.BaseVector;

public class PlanetoidTest {
    @Test
    public void TestImpactEnergyResting() throws UnitConversionError {
        Planetoid p1 = new Planetoid(1, 1, new BaseVector(Unit.LENGTH), new BaseVector(Unit.ANGLE), new BaseVector(Unit.ACCELERATION), new BaseVector(Unit.ACCELERATION));
        Planetoid p2 = new Planetoid(1, 1, new BaseVector(1, 0, 0, Unit.LENGTH), new BaseVector(Unit.ANGLE), new BaseVector(Unit.ACCELERATION), new BaseVector(Unit.ACCELERATION));

        Collision c1 = p1.calculateCollision(p2);
        Assert.assertEquals(0.0, c1.impactEnergy.getValue().doubleValue(), 0.0);
    }

    @Test
    public void TestImpactEnergyImpact() throws UnitConversionError {
        Planetoid p1 = new Planetoid(1, 1, new BaseVector(0, 0, 0, Unit.LENGTH), new BaseVector(Unit.ANGLE), new BaseVector(0, 0, 0, Unit.ACCELERATION), new BaseVector(Unit.ACCELERATION));
        Planetoid p2 = new Planetoid(1, 1, new BaseVector(1, 0, 0, Unit.LENGTH), new BaseVector(Unit.ANGLE), new BaseVector(-1, 0, 0, Unit.ACCELERATION), new BaseVector(Unit.ACCELERATION));

        Collision c1 = p1.calculateCollision(p2);
        // 1/2 * 2 * 1 = 1
        Assert.assertEquals(1.0, c1.impactEnergy.getValue().doubleValue(), 0.0);
    }

    @Test
    public void TestImpactEnergyVelocity() throws UnitConversionError {
        Planetoid p1 = new Planetoid(1, 1, new BaseVector(0, 0, 0, Unit.LENGTH), new BaseVector(Unit.ANGLE), new BaseVector(-5, 0, 0, Unit.ACCELERATION), new BaseVector(Unit.ACCELERATION));
        Planetoid p2 = new Planetoid(1, 1, new BaseVector(1, 0, 0, Unit.LENGTH), new BaseVector(Unit.ANGLE), new BaseVector(-10, 0, 0, Unit.ACCELERATION), new BaseVector(Unit.ACCELERATION));

        Collision c1 = p1.calculateCollision(p2);
        // 1/2 * 2 * 25 = 25
        Assert.assertEquals(25.0, c1.impactEnergy.getValue().doubleValue(), 0.0);
    }

    @Test
    public void TestImpactPointAngled() throws UnitConversionError {
        Planetoid p1 = new Planetoid(1, 1, new BaseVector(0, 0, 0, Unit.LENGTH), new BaseVector(Unit.ANGLE), new BaseVector(1, 1, 1, Unit.ACCELERATION), new BaseVector(Unit.ACCELERATION));
        Planetoid p2 = new Planetoid(1, 1, new BaseVector(1, 1, 1, Unit.LENGTH), new BaseVector(Unit.ANGLE), new BaseVector(-1, -1, -1, Unit.ACCELERATION), new BaseVector(Unit.ACCELERATION));

        Collision c1 = p1.calculateCollision(p2);

        Assert.assertEquals(0.5, c1.impactPointFromA.getX().getValue().doubleValue(), 0.0);
        Assert.assertEquals(0.5, c1.impactPointFromA.getY().getValue().doubleValue(), 0.0);
        Assert.assertEquals(0.5, c1.impactPointFromA.getZ().getValue().doubleValue(), 0.0);

        Assert.assertEquals(-0.5, c1.impactPointFromB.getX().getValue().doubleValue(), 0.0);
        Assert.assertEquals(-0.5, c1.impactPointFromB.getY().getValue().doubleValue(), 0.0);
        Assert.assertEquals(-0.5, c1.impactPointFromB.getZ().getValue().doubleValue(), 0.0);
    }
}
