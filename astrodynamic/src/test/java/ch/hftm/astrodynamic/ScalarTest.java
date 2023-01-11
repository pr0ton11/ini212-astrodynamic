/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

package ch.hftm.astrodynamic;

import org.junit.Assert;
import org.junit.Test;

import ch.hftm.astrodynamic.utils.*;

public class ScalarTest {
    @Test
    public void TestLength() throws UnitConversionError {
        LengthScalar ls = new LengthScalar(15);

        Scalar rs = ls.multiply(new LengthScalar(2));

        Assert.assertTrue(rs.getUnit() == Unit.AREA);
        Assert.assertEquals(30.0, rs.getValue().doubleValue(), 0.0);

        rs = ls.multiply(new AreaScalar(3));

        Assert.assertTrue(rs.getUnit() == Unit.VOLUME);
        Assert.assertEquals(45.0, rs.getValue().doubleValue(), 0.0);

        rs = ls.multiply(new BaseScalar(4));

        Assert.assertTrue(rs.getUnit() == Unit.LENGTH);
        Assert.assertEquals(60.0, rs.getValue().doubleValue(), 0.0);
    }
}
