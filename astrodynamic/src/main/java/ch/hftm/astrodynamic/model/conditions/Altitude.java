package ch.hftm.astrodynamic.model.conditions;

import ch.hftm.astrodynamic.model.EndType;
import ch.hftm.astrodynamic.model.Simulation;
import ch.hftm.astrodynamic.scalar.LengthScalar;
import ch.hftm.astrodynamic.utils.Scalar;

// we win if we are above this altitude TODO: set planet, generalize win/loss
public class Altitude extends BaseCondition {
    Scalar minHeigth;

    Altitude(LengthScalar minHeigth) {
        this.minHeigth = minHeigth;
    }

    @Override
    public boolean conditionMet(Simulation sim) {
        return false;
    }

    @Override
    public EndType getEndType() {
        return EndType.SUCCESS;
    }
    
}
