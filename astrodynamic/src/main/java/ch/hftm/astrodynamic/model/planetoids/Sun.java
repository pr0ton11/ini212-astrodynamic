package ch.hftm.astrodynamic.model.planetoids;

import ch.hftm.astrodynamic.model.Simulation;
import ch.hftm.astrodynamic.physics.*;
import ch.hftm.astrodynamic.scalar.LengthScalar;
import ch.hftm.astrodynamic.scalar.MassScalar;
import ch.hftm.astrodynamic.scalar.UnitlessScalar;
import ch.hftm.astrodynamic.utils.*;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */


/* the sun
* mass: 5.97219e+24 kg
* zero point: 6.371e+6 m
* TODO: axial tilt
*/
public class Sun extends Planetoid {
    public Sun() {
        super(
            new LengthScalar(new Quad(6.957).multiply(Quad.TEN.pow(9))),
            new MassScalar(new Quad(1.9885).multiply(Quad.TEN.pow(30))),
            new BaseVector(Unit.LENGTH),
            new BaseVector(Unit.ANGLE),
            new BaseVector(Unit.VELOCITY),
            new BaseVector(Unit.ANGULAR_VELOCITY),
            new LengthScalar(new Quad(3.0, 6)), // 3000 km, wild guess according to https://en.wikipedia.org/wiki/Solar_transition_region#/media/File:Temperature-height_graph_for_solar_atmosphere.jpg
            AtmosphereModel.QUADRATIC_FALLOFF,
            new UnitlessScalar(0.0)
            );

        setName("Sun");
        setDescription("Also called Helios or Sol");
    }

    // shorthand to add with correct parameters
    public static void addToSimulation(Simulation sim) {
        Sun sun = new Sun();
        if (sim.getAstronomicalObjectByName(sun.getName()) != null) {
            return; // we already have a moon in this simulation
        }

        sim.addPlanetoid(sun);
    }
}
