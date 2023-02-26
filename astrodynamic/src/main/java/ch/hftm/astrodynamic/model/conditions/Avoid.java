package ch.hftm.astrodynamic.model.conditions;

import ch.hftm.astrodynamic.model.EndType;
import ch.hftm.astrodynamic.scalar.LengthScalar;
import ch.hftm.astrodynamic.utils.Named;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// mission is lost if the distance to the named object is below the set distance
public class Avoid extends DistanceContstraint {
    public Avoid(LengthScalar distance, Named referenceObject) {
        super(distance, referenceObject, false, EndType.FAILURE);
    }
}
