package ch.hftm.astrodynamic.model.conditions;

import ch.hftm.astrodynamic.model.EndType;
import ch.hftm.astrodynamic.scalar.LengthScalar;
import ch.hftm.astrodynamic.utils.Named;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// to win the distance must be over the set distance in relation to the named object
public class Depart extends DistanceContstraint {
    public Depart(LengthScalar distance, Named referenceObject) {
        super(distance, referenceObject, true, EndType.SUCCESS);
    }
}
