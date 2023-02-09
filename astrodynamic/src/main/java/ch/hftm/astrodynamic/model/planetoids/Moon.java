package ch.hftm.astrodynamic.model.planetoids;

import java.util.logging.Logger;

import ch.hftm.astrodynamic.model.Simulation;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

import ch.hftm.astrodynamic.physics.*;
import ch.hftm.astrodynamic.scalar.LengthScalar;
import ch.hftm.astrodynamic.scalar.MassScalar;
import ch.hftm.astrodynamic.utils.*;

/* planetoid moon (luna) 
* mass: 7.34767309e+22 kg
* zero point: 1.7374e+6 m
* TODO: axial tilt
*/
public class Moon extends Planetoid {

    private static Logger log = Log.build("model/planetoids/Moon");

    public Moon() {
        super(
            new LengthScalar(new Quad(1.7374, 6)), 
            new MassScalar(new Quad(7.34767309, 22)), 
            new BaseVector(Unit.LENGTH), 
            new BaseVector(Unit.ANGLE), 
            new BaseVector(Unit.VELOCITY), 
            new BaseVector(Unit.ANGULAR_VELOCITY)
        );

        setName("Moon");
        setDescription("Our faithfull companion");
    }

    // shorthand to add with correct parameters
    public static void addToSimulation(Simulation sim) {
        Moon moon = new Moon();
        if (sim.getAstronomicalObjectByName(moon.getName()) != null) {
            return; // we already have a moon in this simulation
        }

        // we try to position ourself relative to the earth
        BaseAstronomicalObject earth = sim.getAstronomicalObjectByName("Earth");
        if (earth != null) {
            try {
                moon.setPosition(earth.getPosition().add(new BaseVector(new Quad(3.85, 8), new Quad(), new Quad(), Unit.LENGTH)));
                moon.setVelocity(new BaseVector(new Quad(), new Quad(1.025, 3), new Quad(), Unit.VELOCITY).add(earth.getVelocity()));
            } catch (UnitConversionError ex) {
                log.severe(ex.getMessage());
            }
        }

        sim.addPlanetoid(moon);
    }
}
