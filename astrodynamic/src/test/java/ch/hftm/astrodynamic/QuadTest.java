package ch.hftm.astrodynamic;

import org.eclipse.yasson.internal.serializer.ObjectSerializer;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import ch.hftm.astrodynamic.utils.Quad;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// Tests for the Quad class
public class QuadTest {

    // Quad initialization
    @Test
    public void TestInitialization() {
        ArrayList<Quad> quadList = new ArrayList<Quad>();
        quadList.add(new Quad());
        quadList.add(new Quad(0.0));
        quadList.add(new Quad(0.0, 0.0));
        quadList.add(new Quad(new Quad(0.0)));
        quadList.add(new Quad(0));
        //quadList.add(new Quad("0.0")); // this throws a string error
        //quadList.add(new Quad("0.0E")); // invalid exponent

        
        quadList.forEach((quad) -> Assert.assertTrue("All Quads are Quads", quad.getClass() == Quad.class));
        quadList.forEach((quad) -> Assert.assertTrue("All Quads are zero", quad.isZero()));
    }

    // Quad Serialization with Object Streams
    @Test
    public void TestSerialization() {
        String fileName = "test.ser";
        Quad q1 = new Quad(123.456);
        Quad q2 = new Quad();
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(q1);
            oos.close();
            fos.close();

            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            q2 = (Quad)ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {

        } finally {
            // clean up file
            File serFile = new File(fileName);
            if (serFile.exists()) {
                serFile.delete();
            }
        }

        Assert.assertEquals(q1.doubleValue(), q2.doubleValue(), 0.0);
    }

    // Quad Addition
    @Test
    public void TestAddition() {
        Quad q1 = new Quad(15);
        Quad q2 = new Quad(17.5);

        // order of addition should not matter
        Assert.assertEquals(32.5, q1.add(q2).doubleValue(), 0.0);
        Assert.assertEquals(32.5, q2.add(q1).doubleValue(), 0.0);
    }

    // Quad Subtraction
    @Test
    public void TestSubtraction() {
        Quad q1 = new Quad(12);
        Quad q2 = new Quad(7);

        // order of subtraction should matter
        Assert.assertEquals(5.0, q1.subtract(q2).doubleValue(), 0.0);
        Assert.assertEquals(-5.0, q2.subtract(q1).doubleValue(), 0.0);
    }

    // Quad Multiplication
    @Test
    public void TestMultiplication() {
        Quad q1 = new Quad(10);
        Quad q2 = new Quad(3.3);
        Quad qNegative = new Quad(-10);

        // order of multiplication should not matter
        Assert.assertEquals(33.0, q1.multiply(q2).doubleValue(), 0.0);
        Assert.assertEquals(33.0, q2.multiply(q1).doubleValue(), 0.0);

        // check if signed multiplication is handled correct
        Assert.assertEquals(-100.0, q1.multiply(qNegative).doubleValue(), 0.0);
        Assert.assertEquals(-33.0, q2.multiply(qNegative).doubleValue(), 0.0);
        Assert.assertEquals(100.0, qNegative.multiply(qNegative).doubleValue(), 0.0);
    }

    // Quad Division
    @Test
    public void TestDivision() {
        Quad q1 = new Quad(10);
        Quad qZero = new Quad(0);
        Quad q2 = new Quad(3);

        // division by Zero is not a number
        Assert.assertTrue(q1.divide(qZero).isNaN());

        // 10/3 = 3.333...
        Assert.assertEquals(3.333, q1.divide(q2).doubleValue(), 0.01);

        // 3/10 = 0.3
        Assert.assertEquals(0.3, q2.divide(q1).doubleValue(), 0.0);
    }
}
