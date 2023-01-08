package ch.hftm.astrodynamic;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;

import ch.hftm.astrodynamic.utils.Quad;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// Tests for the Quad class
public class QuadTest extends TestCase {
    
    // Constructor
    public QuadTest(String testName)
    {
        super( testName );
    }

    // Returns the full test suite
    public static Test suite()
    {
        return new TestSuite(QuadTest.class);
    }

    /*
    * Test cases
    */

    // Quad initialization
    public void TestInitialization() {
        ArrayList<Quad> quadList = new ArrayList<Quad>();
        quadList.add(new Quad());
        quadList.add(new Quad(0.0));
        quadList.add(new Quad(0.0, 0.0));
        quadList.add(new Quad(new Quad(0.0)));
        quadList.add(new Quad(0));
        quadList.add(new Quad("0.0"));
        quadList.add(new Quad("0.0E"));

        quadList.forEach((quad) -> assertTrue("All quads are instances of quad", Class.forName("Quad").isInstance(quad)));
        quadList.forEach((quad) -> assertTrue("All quads are initialized as 0", quad.isZero()));
    }

    // Quad Serialization
    public void TestSerialization() {

    }

    // Quad Deserialization
    public void TestDeserialization() {

    }

    // Quad Calculations

}
