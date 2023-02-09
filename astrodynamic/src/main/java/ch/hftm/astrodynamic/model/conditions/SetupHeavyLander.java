package ch.hftm.astrodynamic.model.conditions;

import java.util.logging.Logger;

import ch.hftm.astrodynamic.model.EndType;
import ch.hftm.astrodynamic.model.Simulation;
import ch.hftm.astrodynamic.model.spaceships.HeavyLander;
import ch.hftm.astrodynamic.physics.BaseAstronomicalObject;
import ch.hftm.astrodynamic.scalar.LengthScalar;
import ch.hftm.astrodynamic.scalar.ScalarFactory;
import ch.hftm.astrodynamic.scalar.VelocityScalar;
import ch.hftm.astrodynamic.scalar.ScalarFactory.FittedValue;
import ch.hftm.astrodynamic.utils.BaseVector;
import ch.hftm.astrodynamic.utils.Log;
import ch.hftm.astrodynamic.utils.Named;
import ch.hftm.astrodynamic.utils.Scalar;
import ch.hftm.astrodynamic.utils.Unit;
import ch.hftm.astrodynamic.utils.UnitConversionError;
import ch.hftm.astrodynamic.utils.Vector;

// adds a heavy lander to the simulation on add
public class SetupHeavyLander extends Condition {

    private Logger log = Log.build("model/conditions/DistanceConstraint");

    String landerName;
    Scalar distance;
    String referenceObjectName;

    public SetupHeavyLander(LengthScalar distance, Named referenceObject) {
        this.distance = distance;
        this.referenceObjectName = referenceObject.getName();
        setName("Setup heavy lander");
        setDescription("Heavy lander in orbit");
    }

    @Override
    public void changeSimulationOnAdd(Simulation sim) {
        HeavyLander lander = new HeavyLander();

        BaseAstronomicalObject refObj = sim.getAstronomicalObjectByName(referenceObjectName);

        if (refObj != null) {
            try {
                lander.setPosition(refObj.getPosition().add(new BaseVector(new LengthScalar(), distance.add(refObj.getZeroElevation()), new LengthScalar())));

                Vector velocity = lander.calculateOrbitalSpeed(refObj).add(refObj.getVelocity());
                lander.setVelocity(velocity);

                setDescription(String.format("Heavy lander in %s orbit around %s with orbital speed %s", 
                distance.toFittedString(), 
                referenceObjectName,
                velocity.getLength().toFittedString()));
            } catch (UnitConversionError ex) {
                log.severe(ex.getMessage());
            }
        }

        if (sim.addSpaceship(lander)) {
            landerName = lander.getName();
        } else {
            setDescription("ERROR: Heavy lander in orbit");
        }
    };

    @Override
    public void changeSimulationOnRemove(Simulation sim) {
        if (landerName != null) {
            sim.removeAstronomicalObjectByName(landerName);
        }
    };

    @Override
    public boolean conditionMet(Simulation sim) {
        return false;
    }

    @Override
    public EndType getEndType() {
        return EndType.ERROR;
    }
}
