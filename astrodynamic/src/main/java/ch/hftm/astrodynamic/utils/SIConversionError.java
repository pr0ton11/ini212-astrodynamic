package ch.hftm.astrodynamic.utils;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// Exception for conversion errors in SI units
public class SIConversionError extends Exception {
    
    // Constructor
    public SIConversionError(String msg) {
        super(String.format("SIConversionError: %s", msg));
    }

}
