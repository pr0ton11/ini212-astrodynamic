package ch.hftm.astrodynamic.model.conditions;

import ch.hftm.astrodynamic.model.EndType;
import ch.hftm.astrodynamic.model.Simulation;
import ch.hftm.astrodynamic.utils.Scalar;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// can be a win or loss condition triggered by elapsed mission time
public class TimeContstraint extends BaseCondition {
    Scalar timeGate;
    EndType ending;

    TimeContstraint(Scalar timeGate, EndType ending) {
        super();
        // if we could set something before super we would not need this reimplementation of the description
        String endingText = "Maximum mission duration of ";
        if (ending == EndType.SUCCESS)
            endingText = "Mission won after a duration of ";
        setDescription(endingText + timeGate.toString());

        this.timeGate = timeGate;
        this.ending = ending; // Could be a fail or success depending on circumstances
    }

    // we stop if we reached max allowed time
    @Override
    public boolean conditionMet(Simulation sim) {
        return sim.getTotalTime().ge(timeGate);
    }

    @Override
    public EndType getEndType() {
        return ending;
    }
}
