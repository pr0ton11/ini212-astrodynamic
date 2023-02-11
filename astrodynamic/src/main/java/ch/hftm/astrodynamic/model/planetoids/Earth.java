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

/* planet earth 
* mass: 5.97219e+24 kg
* zero point: 6.371e+6 m
* TODO: axial tilt
*/
public class Earth extends Planetoid {

    private static Logger log = Log.build();

    public Earth() {
        super(
            new LengthScalar(new Quad(6.3781, 6)),
            new MassScalar(new Quad(5.9722, 24)), 
            new BaseVector(Unit.LENGTH), 
            new BaseVector(Unit.ANGLE), 
            new BaseVector(Unit.VELOCITY), 
            new BaseVector(Unit.ANGULAR_VELOCITY)
            );
        setName("Earth");
        setDescription("Our home");
    }

    // shorthand to add with correct parameters
    public static void addToSimulation(Simulation sim) {
        Earth earth = new Earth();
        if (sim.getAstronomicalObjectByName(earth.getName()) != null) {
            return; // we already have an earth in this simulation
        }

        // we try to position ourself relative to the sun
        BaseAstronomicalObject sun = sim.getAstronomicalObjectByName("Sun");
        if (sun != null) {
            try {
                earth.setPosition(sun.getPosition().add(new BaseVector(new Quad(149.6, 11), new Quad(), new Quad(), Unit.LENGTH)));
                earth.setVelocity(new BaseVector(new Quad(), new Quad(2.978, 4), new Quad(), Unit.VELOCITY).add(sun.getVelocity()));
            } catch (UnitConversionError ex) {
                log.severe(ex.getMessage());
            }
        }

        sim.addPlanetoid(earth);
    }
}
