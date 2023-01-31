package ch.hftm.astrodynamic.model.conditions;

import com.epam.deltix.dfp.Decimal;

import ch.hftm.astrodynamic.model.EndType;
import ch.hftm.astrodynamic.model.Simulation;
import ch.hftm.astrodynamic.physics.AstronomicalObject;
import ch.hftm.astrodynamic.physics.BaseAstronomicalObject;
import ch.hftm.astrodynamic.physics.Planetoid;
import ch.hftm.astrodynamic.physics.Spaceship;
import ch.hftm.astrodynamic.scalar.LengthScalar;
import ch.hftm.astrodynamic.scalar.ScalarFactory;
import ch.hftm.astrodynamic.utils.Named;
import ch.hftm.astrodynamic.utils.Scalar;
import ch.hftm.astrodynamic.utils.Unit;
import ch.hftm.astrodynamic.utils.UnitConversionError;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// can be a win or loss condition triggered by elapsed mission time
public class DistanceContstraint extends Condition {
    Scalar altitudeGate;
    boolean checkForGreater;
    EndType ending;
    String referenceObjectName; // we only save the name to later reference in the simulation to have weak binding

    DistanceContstraint(Scalar altitudeGate, String referenceObjectName, boolean checkForGreater, EndType ending) {
        super();
        // if we could set something before super we would not need this reimplementation of the description
        StringBuilder descriptionTextBuilder = new StringBuilder();
        descriptionTextBuilder.append(String.format(
            "Altitude of %s over %s results in ",
            altitudeGate.toFittedString(),
            referenceObjectName
        ));

        if (ending == EndType.SUCCESS)
            descriptionTextBuilder.append("victory");
        else
            descriptionTextBuilder.append("loss");
        setDescription(descriptionTextBuilder.toString());

        this.altitudeGate = altitudeGate;
        this.checkForGreater = checkForGreater;
        this.ending = ending; // Could be a fail or success depending on circumstances
        this.referenceObjectName = referenceObjectName; // we only save the name to later reference in the simulation to have weak binding
    }

    DistanceContstraint(Scalar altitudeGate, Named referenceObject, boolean checkForGreater, EndType ending) {
        this(altitudeGate, referenceObject.getName(), checkForGreater, ending);
    }

    // we stop if we trespassed the gated distance
    @Override
    public boolean conditionMet(Simulation sim) {
        Spaceship ship = sim.getPlayerControlledVessel();
        BaseAstronomicalObject referenceObject = sim.getAstronomicalObjectByName(referenceObjectName);

        if (ship == null) {
            return false;
        }

        if (referenceObject == null) {
            return false;
        }

        try {
            Scalar heigth = ship.getDistance(referenceObject).subtract(referenceObject.getZeroElevation());
            if (checkForGreater) {
                return heigth.ge(altitudeGate);
            } else {
                return heigth.le(altitudeGate);
            }
        } catch (UnitConversionError ex) {
            System.out.println(ex);
        }

        return false;
    }

    @Override
    public EndType getEndType() {
        return ending;
    }
}
