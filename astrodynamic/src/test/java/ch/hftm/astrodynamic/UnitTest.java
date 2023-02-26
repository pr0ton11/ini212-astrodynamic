package ch.hftm.astrodynamic;

import org.junit.Assert;
import org.junit.Test;

import ch.hftm.astrodynamic.utils.UnitConversionError;
import ch.hftm.astrodynamic.utils.Unit;
import ch.hftm.astrodynamic.scalar.ScalarFactory;
import ch.hftm.astrodynamic.scalar.MassScalar;
import ch.hftm.astrodynamic.utils.Scalar;


/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

 // Tests for the SI classes
public class UnitTest {
    
    // test addition
    @Test
    public void TestAddition() throws UnitConversionError {
        Scalar sv1 = ScalarFactory.create(15, Unit.UNITLESS);
        Scalar sv2U = ScalarFactory.create(2, Unit.LENGTH);
        Scalar sv1U = ScalarFactory.create(15, Unit.LENGTH);
        Scalar sv2 = ScalarFactory.create(2, Unit.UNITLESS);

        Scalar svR = new MassScalar(0);

        try {
            svR = sv1.add(sv2);
        } catch (UnitConversionError e) {
            Assert.assertTrue(String.format("Scalar to Unit casting exception: %s", e.toString()), false);
        }

        Assert.assertEquals(17.0, svR.getValue().doubleValue(), 0.0);
        Assert.assertTrue("Is dimensionless", svR.getUnit() == Unit.UNITLESS);

        try {
            svR = sv1.add(sv2U);
            Assert.assertTrue("Scalar to Unit casting exception expected but not thrown", false);
        } catch (UnitConversionError e) {
            
        }

        try {
            svR = sv1U.add(sv2U);
        } catch (UnitConversionError e) {
            Assert.assertTrue(String.format("Scalar to Unit casting exception: %s", e.toString()), false);
        }

        Assert.assertEquals(17.0, svR.getValue().doubleValue(), 0.0);
        Assert.assertTrue("Is length unit", svR.getUnit() == Unit.LENGTH);

        try {
            svR = sv1U.add(sv2);
            Assert.assertTrue("Scalar to Unit casting exception expected but not thrown", false);
        } catch (UnitConversionError e) {
            
        }
    }

    // test multiplication
    @Test
    public void TestMultiplicationUnitlessUnitless() throws UnitConversionError {
        Scalar sv1 = ScalarFactory.create(15, Unit.UNITLESS);
        Scalar sv2U = ScalarFactory.create(2, Unit.LENGTH);
        Scalar sv1U = ScalarFactory.create(15, Unit.LENGTH);
        Scalar sv2 = ScalarFactory.create(2, Unit.UNITLESS);

        Scalar svR = ScalarFactory.create(0, Unit.MASS);

        
        svR = sv1.multiply(sv2);

        Assert.assertEquals(30.0, svR.getValue().doubleValue(), 0.0);
        Assert.assertEquals(Unit.UNITLESS, svR.getUnit());

        svR = sv1.multiply(sv2U);

        Assert.assertEquals(30.0, svR.getValue().doubleValue(), 0.0);
        Assert.assertEquals(Unit.LENGTH, svR.getUnit());

        svR = sv1U.multiply(sv2);

        Assert.assertEquals(30.0, svR.getValue().doubleValue(), 0.0);
        Assert.assertEquals(Unit.LENGTH, svR.getUnit());

        svR = sv1U.multiply(sv2U);

        Assert.assertEquals(30.0, svR.getValue().doubleValue(), 0.0);
        Assert.assertEquals(Unit.AREA, svR.getUnit());
    }

    // test multiplication, unitless * length = length
    @Test
    public void TestMultiplicationUnitlessUnit() throws UnitConversionError {
        Scalar sv1 = ScalarFactory.create(15, Unit.UNITLESS);
        Scalar sv2U = ScalarFactory.create(2, Unit.LENGTH);

        Scalar svR = ScalarFactory.create(0, Unit.MASS);

        svR = sv1.multiply(sv2U);

        Assert.assertEquals(30.0, svR.getValue().doubleValue(), 0.0);
        Assert.assertEquals(Unit.LENGTH, svR.getUnit());
    }

    // test multiplication, length * unitless = length
    @Test
    public void TestMultiplicationUnitUnitless() throws UnitConversionError {
        Scalar sv1U = ScalarFactory.create(15, Unit.LENGTH);
        Scalar sv2 = ScalarFactory.create(2, Unit.UNITLESS);

        Scalar svR = ScalarFactory.create(0, Unit.MASS);

        svR = sv1U.multiply(sv2);

        Assert.assertEquals(30.0, svR.getValue().doubleValue(), 0.0);
        Assert.assertEquals(Unit.LENGTH, svR.getUnit());
    }

    // test multiplication, m * m = m^2
    @Test
    public void TestMultiplicationUnitUnit() throws UnitConversionError {
        Scalar sv2U = ScalarFactory.create(2, Unit.LENGTH);
        Scalar sv1U = ScalarFactory.create(15, Unit.LENGTH);

        Scalar svR = ScalarFactory.create(0, Unit.MASS);

        svR = sv1U.multiply(sv2U);

        Assert.assertEquals(30.0, svR.getValue().doubleValue(), 0.0);
        Assert.assertEquals(Unit.AREA, svR.getUnit());
    }

    // test multiplication, time * time throws exception
    @Test(expected = UnitConversionError.class)
    public void TestMultiplicationInvalidUnitUnit() throws UnitConversionError {
        Scalar sv2U = ScalarFactory.create(2, Unit.TIME);
        Scalar sv1U = ScalarFactory.create(15, Unit.TIME);

        Scalar svR = ScalarFactory.create(0, Unit.MASS);

        svR = sv1U.multiply(sv2U);
    }

    // test subtraction
    @Test
    public void TestSubtraction() throws UnitConversionError {
        Scalar sv1 = ScalarFactory.create(15, Unit.UNITLESS);
        Scalar sv2U = ScalarFactory.create(2, Unit.LENGTH);
        Scalar sv1U = ScalarFactory.create(15, Unit.LENGTH);
        Scalar sv2 = ScalarFactory.create(2, Unit.UNITLESS);

        Scalar svR = ScalarFactory.create(0, Unit.MASS);

        try {
            svR = sv1.subtract(sv2);
        } catch (UnitConversionError e) {
            Assert.assertTrue(String.format("Scalar to Unit casting exception: %s", e.toString()), false);
        }

        Assert.assertEquals(13.0, svR.getValue().doubleValue(), 0.0);
        Assert.assertTrue("Is dimensionless", svR.getUnit() == Unit.UNITLESS);

        try {
            svR = sv1.subtract(sv2U);
            Assert.assertTrue("Scalar to Unit casting exception expected but not thrown", false);
        } catch (UnitConversionError e) {
            
        }

        try {
            svR = sv1U.subtract(sv2U);
        } catch (UnitConversionError e) {
            Assert.assertTrue(String.format("Scalar to Unit casting exception: %s", e.toString()), false);
        }

        Assert.assertEquals(13.0, svR.getValue().doubleValue(), 0.0);
        Assert.assertTrue("Is length unit", svR.getUnit() == Unit.LENGTH);

        try {
            svR = sv1U.subtract(sv2);
            Assert.assertTrue("Scalar to Unit casting exception expected but not thrown", false);
        } catch (UnitConversionError e) {
            
        }
    }

    // test division, unitless / unitless = unitless
    @Test
    public void TestDivisionUnitlessUnitless() throws UnitConversionError {
        Scalar sv1 = ScalarFactory.create(15, Unit.UNITLESS);
        Scalar sv2 = ScalarFactory.create(2, Unit.UNITLESS);

        Scalar svR = ScalarFactory.create(0, Unit.MASS);

        svR = sv1.divide(sv2);

        Assert.assertEquals(7.5, svR.getValue().doubleValue(), 0.0);
        Assert.assertEquals(Unit.UNITLESS, svR.getUnit());
    }

    // test division, unitless / length throws exception
    @Test(expected = UnitConversionError.class)
    public void TestDivisionUnitlessUnit() throws UnitConversionError {
        Scalar sv1 = ScalarFactory.create(15, Unit.UNITLESS);
        Scalar sv2U = ScalarFactory.create(2, Unit.LENGTH);

        Scalar svR = ScalarFactory.create(0, Unit.MASS);

        svR = sv1.divide(sv2U);
    }

    // test division, length / unitless = length
    @Test
    public void TestDivisionUnitUnitless() throws UnitConversionError {
        Scalar sv1U = ScalarFactory.create(15, Unit.LENGTH);
        Scalar sv2 = ScalarFactory.create(2, Unit.UNITLESS);

        Scalar svR = ScalarFactory.create(0, Unit.MASS);

        svR = sv1U.divide(sv2);

        Assert.assertEquals(7.5, svR.getValue().doubleValue(), 0.0);
        Assert.assertEquals(Unit.LENGTH, svR.getUnit());
    }

    // test division, length / length = unitless
    @Test
    public void TestDivisionUnitUnit() throws UnitConversionError {
        Scalar sv2U = ScalarFactory.create(2, Unit.LENGTH);
        Scalar sv1U = ScalarFactory.create(15, Unit.LENGTH);

        Scalar svR = ScalarFactory.create(0, Unit.MASS);

        svR = sv1U.divide(sv2U);

        Assert.assertEquals(7.5, svR.getValue().doubleValue(), 0.0);
        Assert.assertEquals(Unit.UNITLESS, svR.getUnit());
    }
    
}
