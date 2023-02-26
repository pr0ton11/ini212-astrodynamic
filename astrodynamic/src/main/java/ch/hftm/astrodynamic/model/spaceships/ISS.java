package ch.hftm.astrodynamic.model.spaceships;

import ch.hftm.astrodynamic.physics.Spaceship;
import ch.hftm.astrodynamic.scalar.LengthScalar;
import ch.hftm.astrodynamic.scalar.MassScalar;
import ch.hftm.astrodynamic.utils.BaseVector;
import ch.hftm.astrodynamic.utils.Unit;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */


// iss is a 45 ton 15m radius ship
public class ISS extends Spaceship {

    public ISS() {
        super(
            new LengthScalar(15), 
            new MassScalar(45000), 
            new BaseVector(Unit.LENGTH),
            new BaseVector(Unit.ANGLE), 
            new BaseVector(Unit.VELOCITY), 
            new BaseVector(Unit.ANGULAR_VELOCITY)
        );
        setName("ISS");
        setDescription("International Space Station");
    }
}
