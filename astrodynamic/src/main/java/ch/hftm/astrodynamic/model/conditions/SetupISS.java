package ch.hftm.astrodynamic.model.conditions;

import java.util.logging.Logger;

import ch.hftm.astrodynamic.model.EndType;
import ch.hftm.astrodynamic.model.Simulation;
import ch.hftm.astrodynamic.model.spaceships.ISS;
import ch.hftm.astrodynamic.physics.BaseAstronomicalObject;
import ch.hftm.astrodynamic.scalar.LengthScalar;
import ch.hftm.astrodynamic.utils.BaseVector;
import ch.hftm.astrodynamic.utils.Log;
import ch.hftm.astrodynamic.utils.Named;
import ch.hftm.astrodynamic.utils.Scalar;
import ch.hftm.astrodynamic.utils.UnitConversionError;
import ch.hftm.astrodynamic.utils.Vector;

// adds a heavy lander to the simulation on add
public class SetupISS extends Condition {

    private Logger log = Log.build();

    String stationName;
    Scalar distance;
    String referenceObjectName;

    public SetupISS(LengthScalar distance, Named referenceObject) {
        this.distance = distance;
        this.referenceObjectName = referenceObject.getName();
        setName("Setup space station");
        setDescription("Space station in orbit");
    }

    @Override
    public void changeSimulationOnAdd(Simulation sim) {
        ISS station = new ISS();

        BaseAstronomicalObject refObj = sim.getAstronomicalObjectByName(referenceObjectName);

        if (refObj != null) {
            try {
                station.setPosition(refObj.getPosition().add(new BaseVector(new LengthScalar(), distance.add(refObj.getZeroElevation()), new LengthScalar())));

                Vector velocity = station.calculateOrbitalSpeed(refObj).add(refObj.getVelocity());
                station.setVelocity(velocity);

                setDescription(String.format("Space station in %s orbit around %s with orbital speed %s", 
                distance.toFittedString(), 
                referenceObjectName,
                velocity.getLength().toFittedString()));
            } catch (UnitConversionError ex) {
                log.severe(ex.getMessage());
            }
        }

        if (sim.addSpaceship(station)) {
            stationName = station.getName();
        } else {
            setDescription("ERROR: Space station in orbit");
        }
    };

    @Override
    public void changeSimulationOnRemove(Simulation sim) {
        if (stationName != null) {
            sim.removeAstronomicalObjectByName(stationName);
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
