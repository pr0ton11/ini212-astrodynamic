package ch.hftm.astrodynamic.model.spaceships;

import ch.hftm.astrodynamic.physics.Spaceship;
import ch.hftm.astrodynamic.scalar.LengthScalar;
import ch.hftm.astrodynamic.scalar.MassScalar;
import ch.hftm.astrodynamic.utils.BaseVector;
import ch.hftm.astrodynamic.utils.Scalar;
import ch.hftm.astrodynamic.utils.Unit;
import ch.hftm.astrodynamic.utils.Vector;

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
    }
}
