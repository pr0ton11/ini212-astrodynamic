package ch.hftm.astrodynamic.model.spaceships;

import ch.hftm.astrodynamic.physics.Spaceship;
import ch.hftm.astrodynamic.scalar.AccelerationScalar;
import ch.hftm.astrodynamic.scalar.LengthScalar;
import ch.hftm.astrodynamic.scalar.MassScalar;
import ch.hftm.astrodynamic.scalar.VelocityScalar;
import ch.hftm.astrodynamic.utils.BaseVector;
import ch.hftm.astrodynamic.utils.Unit;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */


// heavy lander is a 15 ton 2m radius ship
public class HeavyLander extends Spaceship {

    public HeavyLander() {
        super(
            new LengthScalar(2), 
            new MassScalar(15000), 
            new BaseVector(Unit.LENGTH),
            new BaseVector(Unit.ANGLE), 
            new BaseVector(Unit.VELOCITY), 
            new BaseVector(Unit.ANGULAR_VELOCITY)
        );
        setName("Heavy Lander");
        setDescription("Heavy landing module");
        setDeltaV(new VelocityScalar(15000));
        setAcceleration(new AccelerationScalar(200));
    }
}
