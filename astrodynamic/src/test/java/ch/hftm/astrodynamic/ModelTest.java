/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */


package ch.hftm.astrodynamic;

import org.junit.Assert;
import org.junit.Test;

import ch.hftm.astrodynamic.model.planetoids.Earth;
import ch.hftm.astrodynamic.model.planetoids.Moon;
import ch.hftm.astrodynamic.utils.*;
import ch.hftm.astrodynamic.physics.*;
import ch.hftm.astrodynamic.scalar.LengthScalar;
import ch.hftm.astrodynamic.scalar.ScalarFactory;
import ch.hftm.astrodynamic.scalar.VelocityScalar;
 
 public class ModelTest {
    @Test
    public void TestEarthMoonOrbit() throws UnitConversionError{
        Planetoid earth = new Earth();
        Planetoid moon = new Moon();
        Scalar oneSecond = ScalarFactory.create(1, Unit.TIME); // needed for acceleration/velocity calculation
        int secondsTestTotal = 32000000;
        int secondsInDay = 86400;

        // average distance earth to moon is 3.85e+8 m
        moon.setPosition(earth.getPosition().add(new BaseVector(new Quad(3.85, 8), new Quad(), new Quad(), Unit.LENGTH)));

        // average speed of moon is 1.022e+3 m/s
        moon.setVelocity(new BaseVector(new Quad(), new Quad(1.025, 3), new Quad(), Unit.VELOCITY).add(earth.getVelocity()));

        // seconds in a year: 3.154e+7
        for (int i = 0; i < secondsTestTotal; i++) {
            Vector force = earth.calculateGravitationalForce(moon);

            Vector earthAcceleration = earth.calculateAccelerationFromForce(force);

            // we calculated the force vector from earth so we need to invert the direction for moon
            Vector moonAcceleration = moon.calculateAccelerationFromForce(force.invert());

            // add acceleration to the velocity
            earth.applyAcceleration(earthAcceleration, oneSecond);
            moon.applyAcceleration(moonAcceleration, oneSecond);

            // move objects by adding velocity to position
            earth.applyVelocity(oneSecond);
            moon.applyVelocity(oneSecond);

        }

        Scalar expectedDistance = new LengthScalar(new Quad(3.85, 8));
        Scalar expectedVelocity = new VelocityScalar(new Quad(1.025, 3));
        Quad maxDistanceError = new Quad(3.0, 6);
        Quad maxVelocityError = new Quad(1.0, 3);

        Assert.assertTrue(
            String.format("Earth/Moon distance expected %s, measured %s", expectedDistance.toString(), moon.getDistance(earth).toString()),
            moon.getDistance(earth).almostEquals(expectedDistance, maxDistanceError)
        );

        Assert.assertTrue(
            String.format("Moon velocity expected %s, measured %s", expectedVelocity.toString(), moon.getVelocity().getLength().toString()),
            moon.getVelocity().getLength().almostEquals(expectedVelocity, maxVelocityError)
        );
    }
}
