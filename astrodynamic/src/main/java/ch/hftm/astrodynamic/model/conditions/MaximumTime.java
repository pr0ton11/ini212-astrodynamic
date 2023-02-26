package ch.hftm.astrodynamic.model.conditions;

import ch.hftm.astrodynamic.model.EndType;
import ch.hftm.astrodynamic.scalar.TimeScalar;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// mission fail after maxtime, helper class to keep gui conditions small in parameters
public class MaximumTime extends TimeContstraint {

    public MaximumTime(TimeScalar timeGate) {
        super(timeGate, EndType.FAILURE);
    }
    
}
