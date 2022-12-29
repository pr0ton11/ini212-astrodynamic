package ch.hftm.astrodynamic.utils;

import java.io.Serializable;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// Implemented based on https://en.wikipedia.org/wiki/Floating-point_arithmetic
public class Quad implements Serializable, Comparable, Cloneable {

    private double hi = 0.0; // Holds the data for the high part of the quad precision double
    private double lo = 0.0; // Holds the data for the low part of the quad precision double

    // Constants for physics calculations
    public static final Quad PI = new Quad(3.141592653589793116e+00,1.224646799147353207e-16);  // PI
    public static final Quad E = new Quad(2.718281828459045091e+00,1.445646891729250158e-16);  // Constant e (natural logaritm base)
    public static final Quad NaN = new Quad(Double.NaN, Double.NaN);  // Constant for NaN (not a number)

    // Default constructor for quad, returns 0 quad
    public Quad() {
        this(0.0);  // Call double constructor
    }

    // Constructor that accepts a double
    public Quad(Double d) {
        // Assign the high part to passed value
        this(d, 0.0);
    }

    // Constructor that accepts two doubles
    // h: high part of the quad (e.g 00.)
    // l: low part of the quad (e.g .00)
    public Quad(Double h, Double l) {
        // Assign values to the object
        this.hi = h;
        this.lo = l;
    }

    // Constructor that accepts a string
    // String gets parsed into a quad
    public Quad(String serializedQuad) throws NumberFormatException {
        this(valueOf(serializedQuad));
    }

    // Constructor that accept another Quad as input
    // Used for string parsing
    public Quad(Quad q) {
        // Assign the 
        this(q.hi, q.lo);
    }

    // Constructor for int values
    public Quad(int i) {
        this(Double.valueOf(i));
    }

    // Returns the closest double number of this quad
    public double doubleValue()
	{
		return hi + lo;
	}

    // Returns the closes int number to the value of this quad
    public int intValue()
	{
		return (int) hi;
	}
	

    // Clones this object
    public Object clone() {
        // This try catch can capture oom errors in the jvm
        try {
			return super.clone();
		}
		catch (CloneNotSupportedException ex) {
            return null;
		}
    }

    // Converts a string value to a Quad instance and returns it
    public static Quad valueOf(String str) throws NumberFormatException { 
	    int i = 0;
        int len = str.length();

        // Check for leading whitespaces
        while(Character.isWhitespace(str.charAt(i))) {
            // Increment index and ignore whitespaces
            i++;
        }

        // Check if value is negative
        boolean negative = false;
        if (i < len) {
            // Check for sign chars (+ / -)
            char signChar = str.charAt(i);
            if (signChar == '+') {
                i++; // Increment char counter to ignore sign
            } else if (signChar == '-') {
                i++; // Increment char counter to ignore sign
                negative = true;  // Update value to be negative
            }
        }

        // Create the new Quad
        Quad quad = new Quad();

        int countDigits = 0;  // Amount of digits
        int countDecPoint = 0;  // Decimal point position
        int exp = 0;  // Exponent

        while(true) {
            if (i > len) {
                // Return from the loop when string lengh has been reached
                break;
            }
            char current = str.charAt(i);
            // Increment counter for next loop
            i++;
            // Check if the current char is a digit
            if(Character.isDigit(current)) {
                double d = current - '0'; // Convert char to double
                quad.sMultiply(new Quad(10.0));  // Shift current value by 1 digit to the left
                quad.sAdd(new Quad(d));  // Add the new digit to the value
                countDigits++;  // Increment digit count
                continue;
            }
            // Check for decimal point
            if (current == '.') {
                // Set the decimal point index
                countDecPoint = countDigits;
                continue;
            }
            if (current == 'e' || current == 'E') {
                String expStr = str.substring(i); // Cut the exponent part from the number
                try {
                    // Convert exponent to an integer
                    exp = Integer.parseInt(expStr);
                } catch (Exception ex) {
                    throw new NumberFormatException(String.format("QuadConversionError: Invalid exponent %s in Quad parse string %s", expStr, str));
                }
                break;
            }
            throw new NumberFormatException(String.format("QuadConversionError: Invalid character %c in Quad parse string %s", current, str));
        }

        // Create final result
        Quad result = quad; // Apply current temporary quad to result
        // Scale the number correctly
        int countPreDecimalDigits = countDigits - countDecPoint - exp;
        if (countPreDecimalDigits == 0) {
            result = quad;  // Reassign result
        } else if (countPreDecimalDigits > 0) {
            Quad scale = new Quad(10.0).pow(countPreDecimalDigits);
            result = quad.divide(scale);
        } else if (countPreDecimalDigits < 0) {
            Quad scale = new Quad(10.0).pow(-countPreDecimalDigits);
            result = quad.multiply(scale);
        }
        if (negative) {
            return result.negate();
        }
        return result;
	}

    // Converts a double value to a Quad instance and returns it
    public static Quad valueOf(double x) { 
        return new Quad(x); 
    }

    // Compare functions

    // Compares the quad to 0
    public boolean isZero() {
		return hi == 0.0 && lo == 0.0;
	}
    // Flag if the value is negative
    public boolean isNegative() {
		return hi < 0.0 || (hi == 0.0 && lo < 0.0);
	}
    // Flag if the value is positive
    public boolean isPositive() {
		return hi > 0.0 || (hi == 0.0 && lo > 0.0);
	}
    // Flag if the value is not a number
    public boolean isNaN() { 
        return Double.isNaN(hi); 
    }
    // Flag if the value equals another quad
    public boolean equals(Quad y) {
		return hi == y.hi && lo == y.lo;
	}
    // Flag if value is greater than
    public boolean gt(Quad y) {
		return (hi > y.hi) || (hi == y.hi && lo > y.lo);
	}
    // Flag if value is greater or equal than
    public boolean ge(Quad y) {
		return (hi > y.hi) || (hi == y.hi && lo >= y.lo);
	}
    // Flag if value is less than
    public boolean lt(Quad y) {
		return (hi < y.hi) || (hi == y.hi && lo < y.lo);
	}
    // Flag if value is less or equal than
    public boolean le(Quad y) {
		return (hi < y.hi) || (hi == y.hi && lo <= y.lo);
	}
    // Compares the quad to another object that gets cast to quad
    // Returns a number representation of the comparison of both quads
    // 0: Could not compare
    // -1: Smaller than
    // +1: Greater than
    public int compareTo(Object o) {
        Quad other = (Quad) o;

        if (hi < other.hi) return -1;
        if (hi > other.hi) return 1;
        if (lo < other.lo) return -1;
        if (lo > other.lo) return 1;
        return 0;
    }

}
