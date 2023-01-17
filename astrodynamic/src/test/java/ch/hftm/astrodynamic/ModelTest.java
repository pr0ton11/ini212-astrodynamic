/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */


 package ch.hftm.astrodynamic;

 import org.junit.Assert;
 import org.junit.Test;
 import org.junit.Ignore;
 
import ch.hftm.astrodynamic.model.*;
import ch.hftm.astrodynamic.utils.*;
import ch.hftm.astrodynamic.physics.*;
import ch.hftm.astrodynamic.scalar.ScalarFactory;
 
 public class ModelTest {
    @Test
    @Ignore
    public void TestEarthMoonOrbit() throws UnitConversionError{
        Planetoid earth = new Earth();
        Planetoid moon = new Moon();
        Scalar oneSecond = ScalarFactory.create(1, Unit.TIME); // needed for acceleration/velocity calculation
        int secondsTestTotal = 32000000;
        int secondsInYear = 31540000;
        int secondsInDay = 86400;
        int secondsInWeek = secondsInDay * 7;

        // average distance earth to moon is 3.85e+8 m
        moon.setPosition(new BaseVector(385000000,0,0, Unit.LENGTH));

        // average speed of moon is 1.022e+3 m/s
        //moon.setVelocity(new BaseVector(0, 0, 1022000, Unit.VELOCITY));
        moon.setVelocity(new BaseVector(0, 0, 0, Unit.VELOCITY));

        // seconds in a year: 3.154e+7
        for (int i = 0; i < secondsTestTotal; i++) {
            Vector force = earth.calculateGravitationalForce(moon);

            Vector earthAcceleration = earth.calculateAccelerationFromForce(force);

            // we calculated the force vector from earth so we need to invert the direction for moon
            Vector moonAcceleration = moon.calculateAccelerationFromForce(force.invert());

            // add acceleration to the velocity
            earth.setVelocity(earth.getVelocity().add(earthAcceleration.multiply(oneSecond)));
            moon.setVelocity(moon.getVelocity().add(moonAcceleration.multiply(oneSecond)));

            // move objects by adding velocity to position
            earth.setPosition(earth.getPosition().add(earth.getVelocity().multiply(oneSecond)));
            moon.setPosition(moon.getPosition().add(moon.getVelocity().multiply(oneSecond)));

            if (i%secondsInWeek == 0) {
                System.out.println(String.format("%03d/%03d: moon velocity: %08.6f moon distance: %08.6f", i/secondsTestTotal, secondsInYear/secondsTestTotal, moon.getVelocity().getLength().getValue().doubleValue(), earth.getPosition().subtract(moon.getPosition()).getLength().getValue().doubleValue()));
            }
        }

        System.out.println(moon.getPosition().toString());
    }
}
