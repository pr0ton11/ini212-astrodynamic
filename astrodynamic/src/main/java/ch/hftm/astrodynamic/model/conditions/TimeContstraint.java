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
    Scalar maximumMissionTime;
    EndType ending;

    TimeContstraint(Scalar maximumMissionTime, EndType ending) {
        super("Timed", "");
        // if we could set something before super we would not need this reimplementation of the description
        String endingText = "Maximum mission duration of ";
        if (ending == EndType.SUCCESS)
            endingText = "Mission won after a duration of ";
        setDescription(endingText + maximumMissionTime.toString());

        this.maximumMissionTime = maximumMissionTime;
        this.ending = ending; // Could be a fail or success depending on circumstances
    }

    // we stop if we reached max allowed time
    @Override
    public boolean conditionMet(Simulation sim) {
        return sim.getTotalTime().ge(maximumMissionTime);
    }

    @Override
    public EndType getEndType() {
        return ending;
    }
}
