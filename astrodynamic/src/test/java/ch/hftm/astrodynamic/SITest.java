package ch.hftm.astrodynamic;

import org.junit.Assert;
import org.junit.Test;

import ch.hftm.astrodynamic.utils.SIConversionError;
import ch.hftm.astrodynamic.utils.SIUnit;
import ch.hftm.astrodynamic.utils.SIValue;
import ch.hftm.astrodynamic.utils.Scalar;


/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

 // Tests for the SI classes
public class SITest {
    // test addition
    @Test
    public void TestAddition() {
        SIValue sv1 = new SIValue(15, SIUnit.UNITLESS);
        SIValue sv2U = new SIValue(2, SIUnit.LENGTH);
        SIValue sv1U = new SIValue(15, SIUnit.LENGTH);
        SIValue sv2 = new SIValue(2, SIUnit.UNITLESS);

        Scalar svR = new SIValue(0, SIUnit.MASS);

        try {
            svR = sv1.addition(sv2);
        } catch (SIConversionError e) {
            Assert.assertTrue(String.format("Scalar to SIUnit casting exception: %s", e.toString()), false);
        }

        Assert.assertEquals(17.0, svR.getValue().doubleValue(), 0.0);
        Assert.assertTrue("Is dimensionless", svR.getUnit() == SIUnit.UNITLESS);

        try {
            svR = sv1.addition(sv2U);
            Assert.assertTrue("Scalar to SIUnit casting exception expected but not thrown", false);
        } catch (SIConversionError e) {
            
        }

        try {
            svR = sv1U.addition(sv2U);
        } catch (SIConversionError e) {
            Assert.assertTrue(String.format("Scalar to SIUnit casting exception: %s", e.toString()), false);
        }

        Assert.assertEquals(17.0, svR.getValue().doubleValue(), 0.0);
        Assert.assertTrue("Is length unit", svR.getUnit() == SIUnit.LENGTH);

        try {
            svR = sv1U.addition(sv2);
            Assert.assertTrue("Scalar to SIUnit casting exception expected but not thrown", false);
        } catch (SIConversionError e) {
            
        }
    }

    // test multiplication
    @Test
    public void TestMultiplication() {
        // TODO: calculation between units should result into correct units (eg. m * m = m2, m * unitless = m)
        SIValue sv1 = new SIValue(15, SIUnit.UNITLESS);
        SIValue sv2U = new SIValue(2, SIUnit.LENGTH);
        SIValue sv1U = new SIValue(15, SIUnit.LENGTH);
        SIValue sv2 = new SIValue(2, SIUnit.UNITLESS);

        Scalar svR = new SIValue(0, SIUnit.MASS);

        try {
            svR = sv1.multiplication(sv2);
        } catch (SIConversionError e) {
            Assert.assertTrue(String.format("Scalar to SIUnit casting exception: %s", e.toString()), false);
        }

        Assert.assertEquals(30.0, svR.getValue().doubleValue(), 0.0);
        Assert.assertTrue("Is dimensionless", svR.getUnit() == SIUnit.UNITLESS);

        try {
            svR = sv1.multiplication(sv2U);
        } catch (SIConversionError e) {
            Assert.assertTrue(String.format("Scalar to SIUnit casting exception: %s", e.toString()), false);
        }

        Assert.assertEquals(30.0, svR.getValue().doubleValue(), 0.0);
        Assert.assertTrue("Is length unit", svR.getUnit() == SIUnit.LENGTH);

        try {
            svR = sv1U.multiplication(sv2);
        } catch (SIConversionError e) {
            Assert.assertTrue(String.format("Scalar to SIUnit casting exception: %s", e.toString()), false);
        }

        Assert.assertEquals(30.0, svR.getValue().doubleValue(), 0.0);
        Assert.assertTrue("Is length unit", svR.getUnit() == SIUnit.LENGTH);

        try {
            svR = sv1U.multiplication(sv2U);
        } catch (SIConversionError e) {
            Assert.assertTrue(String.format("Scalar to SIUnit casting exception: %s", e.toString()), false);
        }

        Assert.assertEquals(30.0, svR.getValue().doubleValue(), 0.0);
        Assert.assertTrue("Is length unit", svR.getUnit() == SIUnit.LENGTH);
    }

    // test subtraction
    @Test
    public void TestSubtraction() {
        SIValue sv1 = new SIValue(15, SIUnit.UNITLESS);
        SIValue sv2U = new SIValue(2, SIUnit.LENGTH);
        SIValue sv1U = new SIValue(15, SIUnit.LENGTH);
        SIValue sv2 = new SIValue(2, SIUnit.UNITLESS);

        Scalar svR = new SIValue(0, SIUnit.MASS);

        try {
            svR = sv1.subtraction(sv2);
        } catch (SIConversionError e) {
            Assert.assertTrue(String.format("Scalar to SIUnit casting exception: %s", e.toString()), false);
        }

        Assert.assertEquals(13.0, svR.getValue().doubleValue(), 0.0);
        Assert.assertTrue("Is dimensionless", svR.getUnit() == SIUnit.UNITLESS);

        try {
            svR = sv1.subtraction(sv2U);
            Assert.assertTrue("Scalar to SIUnit casting exception expected but not thrown", false);
        } catch (SIConversionError e) {
            
        }

        try {
            svR = sv1U.subtraction(sv2U);
        } catch (SIConversionError e) {
            Assert.assertTrue(String.format("Scalar to SIUnit casting exception: %s", e.toString()), false);
        }

        Assert.assertEquals(13.0, svR.getValue().doubleValue(), 0.0);
        Assert.assertTrue("Is length unit", svR.getUnit() == SIUnit.LENGTH);

        try {
            svR = sv1U.subtraction(sv2);
            Assert.assertTrue("Scalar to SIUnit casting exception expected but not thrown", false);
        } catch (SIConversionError e) {
            
        }
    }

    // test division
    @Test
    public void TestDivision() {
        // TODO: calculation between units should result into correct units (eg. m2 / unitless = m2, m2 / m = m)
        SIValue sv1 = new SIValue(15, SIUnit.UNITLESS);
        SIValue sv2U = new SIValue(2, SIUnit.LENGTH);
        SIValue sv1U = new SIValue(15, SIUnit.LENGTH);
        SIValue sv2 = new SIValue(2, SIUnit.UNITLESS);

        Scalar svR = new SIValue(0, SIUnit.MASS);

        try {
            svR = sv1.division(sv2);
        } catch (SIConversionError e) {
            Assert.assertTrue(String.format("Scalar to SIUnit casting exception: %s", e.toString()), false);
        }

        Assert.assertEquals(7.5, svR.getValue().doubleValue(), 0.0);
        Assert.assertTrue("Is dimensionless", svR.getUnit() == SIUnit.UNITLESS);

        try {
            svR = sv1.division(sv2U);
        } catch (SIConversionError e) {
            Assert.assertTrue(String.format("Scalar to SIUnit casting exception: %s", e.toString()), false);
        }

        Assert.assertEquals(7.5, svR.getValue().doubleValue(), 0.0);
        Assert.assertTrue("Is length unit", svR.getUnit() == SIUnit.LENGTH);

        try {
            svR = sv1U.division(sv2);
        } catch (SIConversionError e) {
            Assert.assertTrue(String.format("Scalar to SIUnit casting exception: %s", e.toString()), false);
        }

        Assert.assertEquals(7.5, svR.getValue().doubleValue(), 0.0);
        Assert.assertTrue("Is length unit", svR.getUnit() == SIUnit.LENGTH);

        try {
            svR = sv1U.division(sv2U);
        } catch (SIConversionError e) {
            Assert.assertTrue(String.format("Scalar to SIUnit casting exception: %s", e.toString()), false);
        }

        Assert.assertEquals(7.5, svR.getValue().doubleValue(), 0.0);
        Assert.assertTrue("Is length unit", svR.getUnit() == SIUnit.LENGTH);
    }
}
