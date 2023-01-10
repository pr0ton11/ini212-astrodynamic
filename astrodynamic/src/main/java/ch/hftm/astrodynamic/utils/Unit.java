package ch.hftm.astrodynamic.utils;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// Contains all physical units as enum
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
    // Unitless for scalars without unit
    UNITLESS
}
