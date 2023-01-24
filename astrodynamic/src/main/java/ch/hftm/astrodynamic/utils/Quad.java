package ch.hftm.astrodynamic.utils;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

import java.io.Serializable;
import java.math.BigDecimal;

// For reference see https://github.com/epam/DFP
import com.epam.deltix.dfp.Decimal64;
import com.epam.deltix.dfpmath.Decimal64Math;

// Class Quad abstracts Decimal64
public class Quad implements Comparable<Quad>, Serializable {

    private static final long serialVersionUID = 1L;

    // Number that has been assigned to this Quad
    private Decimal64 num = Decimal64.NULL;

    // Default constructor
    // Initializes Quad value to be 0
    public Quad() {
        this.num = Decimal64.ZERO;
    }

    // Constructor that accepts an int
    public Quad(int num) {
        this.num = Decimal64.fromInt(num);
    }

    // Constructor that accepts a double
    public Quad(double num) {
        this.num = Decimal64.fromDouble(num);
    }

    // Constructor that accepts a big decimal
    // Throws an exception if number can not be converted without loosing precision
    public Quad(BigDecimal num) throws IllegalArgumentException {
        this.num = Decimal64.fromBigDecimalExact(num);
    }

    // Constructor that accepts a quad
    public Quad(Quad q) {
        this.num = q.getAsDecimal64();
    }

    // Special constructor that accepts a mantisse and a position of point
    public Quad(int mantissa, int point) {
        this.num = Decimal64.fromFixedPoint(mantissa, point);
    }

    // Special constructor that accepts a mantisse and a position of point
    public Quad(long mantissa, int point) {
        this.num = Decimal64.fromFixedPoint(mantissa, point);
    }

    // Special constructor that accepts a scientific notation
    public Quad(double mantissa, int powerOfTen) {
        this.num = new Quad().multiply(TEN).pow(powerOfTen).getAsDecimal64();
    }

    // Special constructor that accepts Decimal64
    // Used by the internal class to create a new Quad
    public Quad(Decimal64 num) {
        this.num = num;
    }

    // Getter for Decimal64
    // This is used internally in the class to assign a value from another quad
    public Decimal64 getAsDecimal64() {
        return this.num;
    }

    // Getter for a double value
    public Double doubleValue() {
        return num.toDouble();
    }

    // Compares a Quad to another Quad
    // This implements the comparable interface
    // And allows sorting with the included java functions
    public int compareTo(Quad q) {
        return num.compareTo(q.getAsDecimal64());
    }

    // Base math functions
    // Addition
    public Quad add(Quad add) {
        return new Quad(num.add(add.getAsDecimal64()));
    }

    // Substraction
    public Quad subtract(Quad sub) {
        return new Quad(num.subtract(sub.getAsDecimal64()));
    }

    // Multiplication
    public Quad multiply(Quad mul) {
        return new Quad(num.multiply(mul.getAsDecimal64()));
    }

    // Division
    public Quad divide(Quad div) {
        return new Quad(num.divide(div.getAsDecimal64()));
    }

    // Negation
    public Quad negate() {
        return new Quad(num.negate());
    }

    // Power
    public Quad pow(int power) {
        return new Quad(Decimal64Math.pow(num, Decimal64.fromInt(power)));
    }

    // Square Root
    public Quad sqrt() {
        return new Quad(Decimal64Math.sqrt(num));
    }

    // Half the number
    public Quad half() {
        return divide(new Quad(2));
    }

    // Comparator functions
    public boolean isNaN() {
        return num.isNaN() || num.isInfinity();
    }
    public boolean isZero() {
        return num.isZero();
    }
    public boolean isInfinity() {
        return num.isInfinity();
    }
    public boolean isNegativeInfinity() {
        return num.isNegativeInfinity();
    }

    // Mathematical comperators
    public boolean gt(Quad comperator) {
        return num.isGreater(comperator.getAsDecimal64());
    }
    public boolean ge(Quad comperator) {
        return num.isGreaterOrEqual(comperator.getAsDecimal64());
    }
    public boolean lt(Quad comperator) {
        return num.isLess(comperator.getAsDecimal64());
    }
    public boolean le(Quad comperator) {
        return num.isLessOrEqual(comperator.getAsDecimal64());
    }

    public boolean equals(Object comperator) {
        if (comperator instanceof Quad) {
            Quad q = (Quad)comperator;
            return num.isEqual(q.getAsDecimal64());
        }
        return false;
    }

    // Static common number assignments
    public static Quad PI = new Quad(Decimal64.parse("3.141592653589793238462643383279502884197169399375105820974944592307816406286"));
    public static Quad TEN = new Quad(Decimal64.TEN);
    public static Quad TWO = new Quad(Decimal64.TWO);
    public static Quad ONE = new Quad(Decimal64.ONE);
    public static Quad ZERO = new Quad();
    public static Quad INFINITY = new Quad(Decimal64.POSITIVE_INFINITY);
    public static Quad NEGINFINITY = new Quad(Decimal64.NEGATIVE_INFINITY);

}
