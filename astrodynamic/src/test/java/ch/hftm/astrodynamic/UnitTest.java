package ch.hftm.astrodynamic;

import org.junit.Assert;
import org.junit.Test;

import ch.hftm.astrodynamic.utils.UnitConversionError;
import ch.hftm.astrodynamic.utils.Unit;
import ch.hftm.astrodynamic.utils.BaseScalar;
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
    public void TestAddition() {
        BaseScalar sv1 = new BaseScalar(15, Unit.UNITLESS);
        BaseScalar sv2U = new BaseScalar(2, Unit.LENGTH);
        BaseScalar sv1U = new BaseScalar(15, Unit.LENGTH);
        BaseScalar sv2 = new BaseScalar(2, Unit.UNITLESS);

        Scalar svR = new BaseScalar(0, Unit.MASS);

        try {
            svR = sv1.addition(sv2);
        } catch (UnitConversionError e) {
            Assert.assertTrue(String.format("Scalar to Unit casting exception: %s", e.toString()), false);
        }

        Assert.assertEquals(17.0, svR.getValue().doubleValue(), 0.0);
        Assert.assertTrue("Is dimensionless", svR.getUnit() == Unit.UNITLESS);

        try {
            svR = sv1.addition(sv2U);
            Assert.assertTrue("Scalar to Unit casting exception expected but not thrown", false);
        } catch (UnitConversionError e) {
            
        }

        try {
            svR = sv1U.addition(sv2U);
        } catch (UnitConversionError e) {
            Assert.assertTrue(String.format("Scalar to Unit casting exception: %s", e.toString()), false);
        }

        Assert.assertEquals(17.0, svR.getValue().doubleValue(), 0.0);
        Assert.assertTrue("Is length unit", svR.getUnit() == Unit.LENGTH);

        try {
            svR = sv1U.addition(sv2);
            Assert.assertTrue("Scalar to Unit casting exception expected but not thrown", false);
        } catch (UnitConversionError e) {
            
        }
    }

    // test multiplication
    @Test
    public void TestMultiplication() {
        // TODO: calculation between units should result into correct units (eg. m * m = m2, m * unitless = m)
        BaseScalar sv1 = new BaseScalar(15, Unit.UNITLESS);
        BaseScalar sv2U = new BaseScalar(2, Unit.LENGTH);
        BaseScalar sv1U = new BaseScalar(15, Unit.LENGTH);
        BaseScalar sv2 = new BaseScalar(2, Unit.UNITLESS);

        Scalar svR = new BaseScalar(0, Unit.MASS);

        try {
            svR = sv1.multiplication(sv2);
        } catch (UnitConversionError e) {
            Assert.assertTrue(String.format("Scalar to Unit casting exception: %s", e.toString()), false);
        }

        Assert.assertEquals(30.0, svR.getValue().doubleValue(), 0.0);
        Assert.assertTrue("Is dimensionless", svR.getUnit() == Unit.UNITLESS);

        try {
            svR = sv1.multiplication(sv2U);
        } catch (UnitConversionError e) {
            Assert.assertTrue(String.format("Scalar to Unit casting exception: %s", e.toString()), false);
        }

        Assert.assertEquals(30.0, svR.getValue().doubleValue(), 0.0);
        Assert.assertTrue("Is length unit", svR.getUnit() == Unit.LENGTH);

        try {
            svR = sv1U.multiplication(sv2);
        } catch (UnitConversionError e) {
            Assert.assertTrue(String.format("Scalar to Unit casting exception: %s", e.toString()), false);
        }

        Assert.assertEquals(30.0, svR.getValue().doubleValue(), 0.0);
        Assert.assertTrue("Is length unit", svR.getUnit() == Unit.LENGTH);

        try {
            svR = sv1U.multiplication(sv2U);
        } catch (UnitConversionError e) {
            Assert.assertTrue(String.format("Scalar to Unit casting exception: %s", e.toString()), false);
        }

        Assert.assertEquals(30.0, svR.getValue().doubleValue(), 0.0);
        Assert.assertTrue("Is length unit", svR.getUnit() == Unit.LENGTH);
    }

    // test subtraction
    @Test
    public void TestSubtraction() {
        BaseScalar sv1 = new BaseScalar(15, Unit.UNITLESS);
        BaseScalar sv2U = new BaseScalar(2, Unit.LENGTH);
        BaseScalar sv1U = new BaseScalar(15, Unit.LENGTH);
        BaseScalar sv2 = new BaseScalar(2, Unit.UNITLESS);

        Scalar svR = new BaseScalar(0, Unit.MASS);

        try {
            svR = sv1.subtraction(sv2);
        } catch (UnitConversionError e) {
            Assert.assertTrue(String.format("Scalar to Unit casting exception: %s", e.toString()), false);
        }

        Assert.assertEquals(13.0, svR.getValue().doubleValue(), 0.0);
        Assert.assertTrue("Is dimensionless", svR.getUnit() == Unit.UNITLESS);

        try {
            svR = sv1.subtraction(sv2U);
            Assert.assertTrue("Scalar to Unit casting exception expected but not thrown", false);
        } catch (UnitConversionError e) {
            
        }

        try {
            svR = sv1U.subtraction(sv2U);
        } catch (UnitConversionError e) {
            Assert.assertTrue(String.format("Scalar to Unit casting exception: %s", e.toString()), false);
        }

        Assert.assertEquals(13.0, svR.getValue().doubleValue(), 0.0);
        Assert.assertTrue("Is length unit", svR.getUnit() == Unit.LENGTH);

        try {
            svR = sv1U.subtraction(sv2);
            Assert.assertTrue("Scalar to Unit casting exception expected but not thrown", false);
        } catch (UnitConversionError e) {
            
        }
    }

    // test division
    @Test
    public void TestDivision() {
        // TODO: calculation between units should result into correct units (eg. m2 / unitless = m2, m2 / m = m)
        BaseScalar sv1 = new BaseScalar(15, Unit.UNITLESS);
        BaseScalar sv2U = new BaseScalar(2, Unit.LENGTH);
        BaseScalar sv1U = new BaseScalar(15, Unit.LENGTH);
        BaseScalar sv2 = new BaseScalar(2, Unit.UNITLESS);

        Scalar svR = new BaseScalar(0, Unit.MASS);

        try {
            svR = sv1.division(sv2);
        } catch (UnitConversionError e) {
            Assert.assertTrue(String.format("Scalar to Unit casting exception: %s", e.toString()), false);
        }

        Assert.assertEquals(7.5, svR.getValue().doubleValue(), 0.0);
        Assert.assertTrue("Is dimensionless", svR.getUnit() == Unit.UNITLESS);

        try {
            svR = sv1.division(sv2U);
        } catch (UnitConversionError e) {
            Assert.assertTrue(String.format("Scalar to Unit casting exception: %s", e.toString()), false);
        }

        Assert.assertEquals(7.5, svR.getValue().doubleValue(), 0.0);
        Assert.assertTrue("Is length unit", svR.getUnit() == Unit.LENGTH);

        try {
            svR = sv1U.division(sv2);
        } catch (UnitConversionError e) {
            Assert.assertTrue(String.format("Scalar to Unit casting exception: %s", e.toString()), false);
        }

        Assert.assertEquals(7.5, svR.getValue().doubleValue(), 0.0);
        Assert.assertTrue("Is length unit", svR.getUnit() == Unit.LENGTH);

        try {
            svR = sv1U.division(sv2U);
        } catch (UnitConversionError e) {
            Assert.assertTrue(String.format("Scalar to Unit casting exception: %s", e.toString()), false);
        }

        Assert.assertEquals(7.5, svR.getValue().doubleValue(), 0.0);
        Assert.assertTrue("Is length unit", svR.getUnit() == Unit.LENGTH);
    }
}
