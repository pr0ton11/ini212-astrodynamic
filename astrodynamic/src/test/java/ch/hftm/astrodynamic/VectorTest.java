/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

 package ch.hftm.astrodynamic;

 import org.junit.Assert;
 import org.junit.Test;
 
 import ch.hftm.astrodynamic.utils.*;

public class VectorTest {
    @Test
    public void TestInvert() throws UnitConversionError {
        Vector vInitial = new BaseVector(1, 2, 3);
        Vector vExpected = new BaseVector(-1, -2, -3);

        Vector vInverted = vInitial.invert();

        Assert.assertEquals(vExpected, vInverted);
    }
}
