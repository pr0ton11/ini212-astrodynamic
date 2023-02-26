package ch.hftm.astrodynamic.model.conditions;

import ch.hftm.astrodynamic.model.EndType;
import ch.hftm.astrodynamic.scalar.LengthScalar;
import ch.hftm.astrodynamic.utils.Named;
/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// to win a certain distance to a named object must be below the set distance
public class Approach extends DistanceContstraint {
    public Approach(LengthScalar distance, Named referenceObject) {
        super(distance, referenceObject, false, EndType.SUCCESS);
    }
}
