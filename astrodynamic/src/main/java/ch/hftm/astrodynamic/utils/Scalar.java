package ch.hftm.astrodynamic.utils;

import ch.hftm.astrodynamic.scalar.ScalarFactory;
import ch.hftm.astrodynamic.scalar.ScalarFactory.FittedValue;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

public interface Scalar {
    Unit getUnit();
    Quad getValue();

    // Math operations
    Scalar add(Scalar scalar) throws UnitConversionError;
    Scalar subtract(Scalar scalar) throws UnitConversionError;
    Scalar multiply(Scalar scalar) throws UnitConversionError;
    Scalar divide(Scalar scalar) throws UnitConversionError;
    Scalar negate();
    Scalar pow(int exp);

    // Flags
    boolean isUnitless();

    // Mathematical comperators
    boolean gt(Scalar comperator);
    boolean ge(Scalar comperator);
    boolean lt(Scalar comperator);
    boolean le(Scalar comperator);

    // Nice representation for UI
    default String toFittedString() {
        FittedValue fv = ScalarFactory.getFittingUnitsize(this);
        return String.format("%s %s", fv.value.doubleValue().toString(), fv.unitsize);
    };
}
