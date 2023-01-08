package ch.hftm.astrodynamic;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import ch.hftm.astrodynamic.utils.SIConversionError;
import ch.hftm.astrodynamic.utils.SIUnit;
import ch.hftm.astrodynamic.utils.SIValue;


/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

 // Tests for the SI classes
public class SITest extends TestCase {
    
    // Constructor
    public SITest(String testName)
    {
        super( testName );
    }

    // Returns the full test suite
    public static Test suite()
    {
        return new TestSuite(SITest.class);
    }

    /*
    * Test cases
    */

}
