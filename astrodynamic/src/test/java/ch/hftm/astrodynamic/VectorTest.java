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
    public void TestInitialization() throws UnitConversionError {
        Vector vDouble = new BaseVector(1.0, 2.0, 3.5);
        Vector vQuad = new BaseVector(new Quad(1), new Quad(2), new Quad(3.5));
        Vector vScalar = new BaseVector(new UnitlessScalar(1), new UnitlessScalar(2), new UnitlessScalar(3.5));

        Assert.assertEquals(vDouble, vQuad);
        Assert.assertEquals(vDouble, vScalar);
    }

    @Test
    public void TestInvert() throws UnitConversionError {
        Vector vInitial = new BaseVector(1, 2, 3);
        Vector vExpected = new BaseVector(-1, -2, -3);

        Vector vInverted = vInitial.invert();

        Assert.assertEquals(vExpected, vInverted);
    }

    @Test
    public void TestAdd() throws UnitConversionError {
        Vector v1 = new BaseVector(0, -2, 5, Unit.VOLUME);
        Vector v2 = new BaseVector(1, -2, 3.5, Unit.VOLUME);

        Vector vExpected = new BaseVector(1, -4, 8.5, Unit.VOLUME);

        Assert.assertEquals(vExpected, v1.add(v2));
        Assert.assertEquals(vExpected, v2.add(v1));
    }

    @Test
    public void TestSubtract() throws UnitConversionError {
        Vector v1 = new BaseVector(2, 3, 7);
        Vector v2 = new BaseVector(1, 2, 3);
        Vector vExpected1 = new BaseVector(1, 1, 4);
        Vector vExpected2 = new BaseVector(-1, -1, -4);

        Assert.assertEquals(vExpected1, v1.subtract(v2));
        Assert.assertEquals(vExpected2, v2.subtract(v1));
    }

    @Test
    public void TestVectorMultiplication() throws UnitConversionError {
        Vector v1 = new BaseVector(1, 2, -3);
        Vector v2 = new BaseVector(3, 4, -1.5);
        Vector vExpected = new BaseVector(3, 8, 4.5);

        Assert.assertEquals(vExpected, v1.multiply(v2));
        Assert.assertEquals(vExpected, v2.multiply(v1));
    }

    @Test
    public void TestScalarMultiplication() throws UnitConversionError {
        Vector v1 = new BaseVector(1, 2.5, -4);
        Scalar s1 = new UnitlessScalar(3);

        Vector vExpected = new BaseVector(3, 7.5, -12);

        Assert.assertEquals(vExpected, v1.multiply(s1));
        // There is no Scalar.multiply(Vector) method at the moment
    }

    @Test
    public void TestVectorDivision() throws UnitConversionError {
        Vector v1 = new BaseVector(1, 2, -10);
        Vector v2 = new BaseVector(4, 4, -2);
        Vector vExpected1 = new BaseVector(0.25, 0.5, 5);
        Vector vExpected2 = new BaseVector(4.0, 2.0, 0.2);

        Assert.assertEquals(vExpected1, v1.divide(v2));
        Assert.assertEquals(vExpected2, v2.divide(v1));
    }

    @Test
    public void TestScalarDivision() throws UnitConversionError {
        Vector v1 = new BaseVector(1, 2.2, -4);
        Scalar s1 = new UnitlessScalar(2);

        Vector vExpected = new BaseVector(0.5, 1.1, -2);

        Assert.assertEquals(vExpected, v1.divide(s1));
        // There is no Scalar.multiply(Vector) method at the moment
    }
}
