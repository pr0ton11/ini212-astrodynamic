package ch.hftm.astrodynamic.utils;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// Exception for conversion errors in SI units
public class UnitConversionError extends Exception {
    
    // Constructor
    public UnitConversionError(String msg) {
        super(String.format("UnitConversionError: %s", msg));
    }

}
