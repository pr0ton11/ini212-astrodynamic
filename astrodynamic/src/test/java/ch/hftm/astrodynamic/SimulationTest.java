package ch.hftm.astrodynamic;

import org.junit.Test;

import ch.hftm.astrodynamic.model.*;
import ch.hftm.astrodynamic.utils.*;
import ch.hftm.astrodynamic.scalar.ScalarFactory;
 
/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

public class SimulationTest {

    // deltaTime only valid in unit TIME
    @Test(expected = UnitConversionError.class)
    public void TestDeltaTimeChecksUnit() throws UnitConversionError, SimulationRuntimeError{
        Simulation sim = new Simulation();

        sim.simulate(ScalarFactory.create(1, Unit.UNITLESS));
    }

    // deltaTime only valid if greater than zero
    @Test(expected = SimulationRuntimeError.class)
    public void TestDeltaTimeChecksValue() throws UnitConversionError, SimulationRuntimeError{
        Simulation sim = new Simulation();

        sim.simulate(ScalarFactory.create(0, Unit.TIME));
    }

}
