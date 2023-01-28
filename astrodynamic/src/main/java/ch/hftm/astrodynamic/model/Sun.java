package ch.hftm.astrodynamic.model;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

import ch.hftm.astrodynamic.physics.*;
import ch.hftm.astrodynamic.scalar.LengthScalar;
import ch.hftm.astrodynamic.scalar.MassScalar;
import ch.hftm.astrodynamic.utils.*;

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
            new BaseVector(Unit.ANGULAR_VELOCITY));

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
