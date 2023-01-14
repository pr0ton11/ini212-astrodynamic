package ch.hftm.astrodynamic.utils;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// Contains all physical units as enum, we somehow ended up with dimensional analysis?
public enum Unit {
    TIME, // Seconds (s)
    LENGTH, // Meters (m)
    MASS, // Kilogram (kg)
    CURRENT, // Ampere (A)
    TEMPERATURE, // Kelvin (K)
    MOLECULES, // Mol (mol)
    LUNINOSITY, // Candela (cd)
    // Extensions (Implicit)
    VOLUME,  // m³
    AREA, // m²
    FORCE, // N
    ACCELERATION, // m/s²
    VELOCITY, // m/s
    ANGLE, // radian
    CUBIC_MASS, // kg² imaginary unit for gravitational calculations
    M2_DIV_L2, // kg² / m² imaginary unit for gravitational calculations
    F_L2_Mn2, // N * m² * kg² gravitational constant (s⁻² * m³ * kg⁻¹)  
    // Unitless for scalars without unit
    UNITLESS
}
