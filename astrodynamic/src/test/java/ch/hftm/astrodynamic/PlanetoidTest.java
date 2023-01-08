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
import ch.hftm.astrodynamic.utils.Vector3d;

public class PlanetoidTest {
    @Test
    public void TestImpactEnergyResting() {
        Planetoid p1 = new Planetoid(1, 1, new Vector3d(), new Vector3d(), new Vector3d(), new Vector3d());
        Planetoid p2 = new Planetoid(1, 1, new Vector3d(1, 0, 0), new Vector3d(), new Vector3d(), new Vector3d());

        Collision c1 = p1.calculateCollision(p2);

        Assert.assertEquals(0.0, c1.impactEnergy, 0.0);
    }

    @Test
    public void TestImpactEnergyImpact() {
        Planetoid p1 = new Planetoid(1, 1, new Vector3d(), new Vector3d(), new Vector3d(), new Vector3d());
        Planetoid p2 = new Planetoid(1, 1, new Vector3d(1, 0, 0), new Vector3d(), new Vector3d(-1, 0, 0), new Vector3d());

        Collision c1 = p1.calculateCollision(p2);

        // 1/2 * 2 * 1 = 1
        Assert.assertEquals(1.0, c1.impactEnergy, 0.0);
    }

    @Test
    public void TestImpactEnergyVelocity() {
        Planetoid p1 = new Planetoid(1, 1, new Vector3d(), new Vector3d(), new Vector3d(-5, 0, 0), new Vector3d());
        Planetoid p2 = new Planetoid(1, 1, new Vector3d(1, 0, 0), new Vector3d(), new Vector3d(-10, 0, 0), new Vector3d());

        Collision c1 = p1.calculateCollision(p2);

        // 1/2 * 2 * 25 = 25
        Assert.assertEquals(25.0, c1.impactEnergy, 0.0);
    }

    @Test
    public void TestImpactPointAngled() {
        Planetoid p1 = new Planetoid(1, 1, new Vector3d(), new Vector3d(), new Vector3d(1, 1, 1), new Vector3d());
        Planetoid p2 = new Planetoid(1, 1, new Vector3d(1, 1, 1), new Vector3d(), new Vector3d(-1, -1, -1), new Vector3d());

        Collision c1 = p1.calculateCollision(p2);

        Assert.assertEquals(0.5, c1.impactPointFromA.getX(), 0.0);
        Assert.assertEquals(0.5, c1.impactPointFromA.getY(), 0.0);
        Assert.assertEquals(0.5, c1.impactPointFromA.getZ(), 0.0);

        Assert.assertEquals(-0.5, c1.impactPointFromB.getX(), 0.0);
        Assert.assertEquals(-0.5, c1.impactPointFromB.getY(), 0.0);
        Assert.assertEquals(-0.5, c1.impactPointFromB.getZ(), 0.0);
    }
}
