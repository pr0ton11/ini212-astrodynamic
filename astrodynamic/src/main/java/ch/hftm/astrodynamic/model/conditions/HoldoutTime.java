package ch.hftm.astrodynamic.model.conditions;

import ch.hftm.astrodynamic.model.EndType;
import ch.hftm.astrodynamic.scalar.TimeScalar;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// mission success after maxtime, helper class to keep gui conditions small in parameters
public class HoldoutTime extends TimeContstraint {

    public HoldoutTime(TimeScalar timeGate) {
        super(timeGate, EndType.SUCCESS);
    }
    
}

