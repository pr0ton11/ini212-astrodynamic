package ch.hftm.astrodynamic.utils;

import java.io.Serializable;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// Implemented based on https://en.wikipedia.org/wiki/Floating-point_arithmetic
// To be thread safe these values are not directly mutable, but need to be copied to do calculations

public class Quad implements Serializable, Comparable<Quad>, Cloneable {

    private double hi = 0.0; // Holds the data for the high part of the quad precision double
    private double lo = 0.0; // Holds the data for the low part of the quad precision double

    // Constants for physics calculations
    public static final Quad PI = new Quad(3.141592653589793116e+00,1.224646799147353207e-16);  // PI
    public static final Quad E = new Quad(2.718281828459045091e+00,1.445646891729250158e-16);  // Constant e (natural logaritm base)
    public static final Quad NaN = new Quad(Double.NaN, Double.NaN);  // Constant for NaN (not a number)
    private static final double SPLIT = 134217729.0D; // 2^27+1, for IEEE double

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
    @Override
    public boolean equals(Object o) {
        if (o instanceof Quad) {
            Quad oq = (Quad)o;
		    return hi == oq.hi && lo == oq.lo;
        }
        return false;
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
    public int compareTo(Quad o) {
        if (hi < o.hi) return -1;
        if (hi > o.hi) return 1;
        if (lo < o.lo) return -1;
        if (lo > o.lo) return 1;
        return 0;
    }

    // Mathematical functions

    // Addition
    public Quad add(Quad y)	{
		if (isNaN()) return this;
		return (new Quad(this)).sAdd(y);
	}

    // Special addition, addition for 2 quads
    // Should only be called within this class, and only when a new quad gets initialized
    private Quad sAdd(Quad y) {
        double H, h, T, t, S, s, e, f;
        S = hi + y.hi; 
        T = lo + y.lo; 
        e = S - hi; 
        f = T - lo; 
        s = S - e; 
        t = T - f; 
        s = (y.hi - e) + (hi - s); 
        t = (y.lo - f) + (lo - t); 
        e = s + T; 
        H = S + e;
        h = e + (S - H);
        e = t + h;
    
        double zhi = H + e;
        double zlo = e + (H - zhi);
        hi = zhi;
        lo = zlo;
        return this;
	}

    // Subtraction
    public Quad subtract(Quad y) {
		if (isNaN()) return this;
        // Add negative sum to a new quad
		return add(y.negate());
	}

    // Negates the current value
    public Quad negate() {
		if (isNaN()) return this;
		return new Quad(-hi, -lo);
	}

    // Multiplication
    public Quad multiply(Quad y) {
		if (isNaN()) return this;
		if (y.isNaN()) return y;
	    return (new Quad(this)).sMultiply(y);
	}

    // Special multiplication, multipiles 2 quads
    // Should only be called within this class, and only when a new quad gets initialized
    private Quad sMultiply(Quad y) {
        double hx, tx, hy, ty, C, c;
        C = SPLIT * hi; 
        hx = C - hi; 
        c = SPLIT * y.hi;
        hx = C - hx;
        tx = hi - hx; 
        hy = c - y.hi; 
        C = hi * y.hi; 
        hy = c - hy;
        ty = y.hi - hy;
        c = ((((hx * hy - C) + hx * ty) + tx * hy) + tx * ty)+ (hi * y.lo + lo * y.hi);
        double zhi = C + c;
        hx = C - zhi; 
        double zlo = c + hx;
        hi = zhi;
        lo = zlo;
        return this;
	}

    // Division
    public Quad divide(Quad y) {
        double hc, tc, hy, ty, C, c, U, u;
        C = hi / y.hi;
        c = SPLIT * C;
        hc = c - C;
        u = SPLIT * y.hi;
        hc = c - hc;
        tc = C - hc;
        hy = u - y.hi;
        U = C * y.hi;
        hy = u - hy;
        ty = y.hi - hy;
        u = (((hc * hy - U) + hc * ty) + tc * hy) +tc * ty;
        c = ((((hi - U) -u) +lo) -C *y.lo) /y.hi;
        u = C + c; 
        
        double zhi = u; 
        double zlo = (C - u) + c;
        return new Quad(zhi, zlo);
	}

    // Reciprocal
    // Returns the value of 1 / quad values
    public Quad reciprocal() {
        double hc, tc, hy, ty, C, c, U, u;
        C = 1.0 / hi; 
        c = SPLIT * C; 
        hc = c - C;  
        u = SPLIT * hi;
        hc = c - hc;
        tc = C - hc;
        hy = u - hi;
        U = C * hi;
        hy = u - hy;
        ty = hi - hy;
        u = (((hc * hy - U) + hc * ty) + tc * hy) + tc * ty;
        c = ((((1.0 - U) - u)) - C * lo) / hi;
        
        double zhi = C + c; 
        double zlo = (C - zhi) + c;
        return new Quad(zhi, zlo);
	}

    // Floor
    // Returns the smallest (closest to negative infinity) value that is not less than the argument and is equal to a mathematical integer
    public Quad floor()
	{
        if (isNaN()) return NaN;
        double fhi = Math.floor(hi);
        double flo = 0.0;
        if (fhi == hi) {
            flo = Math.floor(lo);
        }
        return new Quad(fhi, flo); 
	}

    // Ceil
    // Returns the smallest (closest to negative infinity) value that is not less than the argument and is equal to a mathematical integer
    public Quad ceil() {
        if (isNaN()) return NaN;
        double fhi=Math.ceil(hi);
        double flo = 0.0;
        if (fhi == hi) {
            flo = Math.ceil(lo);
        }
        return new Quad(fhi, flo); 
	}

    // Halfs the quad
    public Quad half() {
        return this.divide(new Quad(2));
    }

    // Returns an integer indicating the sign of this value
    public int signum() {
        if (isPositive()) return 1;
        if (isNegative()) return -1;
        return 0;
	}

    // Rounds this value to the nearest integer. The value is rounded to an integer by adding 1/2 and taking the floor of the result
    public Quad rint()
	{
		if (isNaN()) return this;
		Quad half = this.add(new Quad(0.5));
		return half.floor();
	}

    // Returns the integer which is largest in absolute value and not further from zero than this value
    public Quad trunc() {
		if (isNaN()) return NaN;
		if (isPositive()) 
			return floor();
		else 
			return ceil();
	}

    // Returns the absolute value
    public Quad abs() {
		if (isNaN()) return NaN;
		if (isNegative()) return negate();
		return new Quad(this);
	}

    // Returns the square of this value
    public Quad sqr() {
		return this.multiply(this);
	}

    // Returns the square root of this value
    public Quad sqrt() {
        if (isZero()) return new Quad(0.0);
	    if (isNegative()) return NaN;
        double x = 1.0 / Math.sqrt(hi);
        double ax = hi * x;
        Quad axdd = new Quad(ax);
        Quad diffSq = this.subtract(axdd.sqr());
        double d2 = diffSq.hi * (x * 0.5);
	    return axdd.add(new Quad(d2));
    }

    // Returns a quad raised to the power of exp
    public Quad pow(int exp) {
		if (exp == 0.0) return valueOf(1.0);
        Quad r = new Quad(this);
        Quad s = valueOf(1.0);
        int n = Math.abs(exp);

        if (n > 1) {
            while (n > 0) {
            if (n % 2 == 1) s.sMultiply(r);
            n /= 2;
            if (n > 0)
                r = r.sqr();
            }
        } else {
            s = r;
        }

        if (exp < 0) return s.reciprocal();
        return s;
	}

    // Determines the magnitude of a number
    private static int magnitude(double x) {
	  double xAbs = Math.abs(x);
	  double xLog10 = Math.log(xAbs) / Math.log(10);
	  int xMag = (int) Math.floor(xLog10);
      // Correction
	  double xApprox = Math.pow(10, xMag);
	  if (xApprox * 10 <= xAbs) xMag += 1;
	  return xMag;
	}

    // Dump Quad to a string
    // Used for debugging and logging
    public String dump() {
		return String.format("Quad<%d,%d>", hi, lo);
	}

    // String conversion
    // Used for serialization
    public String toString() {
        int mag = magnitude(hi);
        if (mag >= -3 && mag <= 20) return toStandardNotation();  // Standard notation when magnitude not big enough
        return toSciNotation();  // Science notation used when magnitude is big enough
	}

    // Returns the string representation of this quad in the standard notation
    public String toStandardNotation() {
        // Check if this returns a special string
        String specialStr = getSpecialNumberString();
        if (specialStr != null) return specialStr;
		
        int[] magnitude = new int[1];
        String sigDigits = extractSignificantDigits(true, magnitude);
        int decimalPointPos = magnitude[0] + 1;

        String num = sigDigits;
        if (sigDigits.charAt(0) == '.') {
            num = "0" + sigDigits;
        }
        else if (decimalPointPos < 0) {
            num = "0." + stringOfChar('0', -decimalPointPos) + sigDigits;
        }
        else if (sigDigits.indexOf('.') == -1) {
            int numZeroes = decimalPointPos - sigDigits.length();
            String zeroes = stringOfChar('0', numZeroes);
            num = sigDigits + zeroes + ".0";
	    }
        // Negative representation
        if (this.isNegative()) return "-" + num;
        return num;
	}

    // Returns the string representation of this quad in the science notation
    public String toSciNotation() {
        // Check for special case 0
		if (isZero()) return "0.0E0";
        // Check if this returns a special string
		String specialStr = getSpecialNumberString();
	    if (specialStr != null) return specialStr;
	  
        int[] magnitude = new int[1];
        String digits = extractSignificantDigits(false, magnitude);
        String expStr = "E" + magnitude[0];
	    // Remove leading 0
        if (digits.charAt(0) == '0') { digits = digits.substring(1); }
	  
        // Add decimal point
        String trailingDigits = "";
        if (digits.length() > 1) trailingDigits = digits.substring(1);
        String digitsWithDecimal = digits.charAt(0) + "." + trailingDigits;
	  
        if (this.isNegative()) return "-" + digitsWithDecimal + expStr;
        return digitsWithDecimal + expStr;
	}

    // Returns string representation for special numbers
    private String getSpecialNumberString() {
        if (isZero())	return "0.0";
        if (isNaN()) 	return "NaN ";
        return "";
	}

    // Returns a string of repeated len * char
    private static String stringOfChar(char ch, int len) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < len; i++) {
            buf.append(ch);
        }
        return buf.toString();
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

    // Returns a string of the significant digits for the string representation of this quad
    private String extractSignificantDigits(boolean insertDecimalPoint, int[] magnitude) {
        Quad ten = new Quad(10.0);
        Quad y = this.abs();
        int mag = magnitude(y.hi);
        Quad scale = ten.pow(mag);
        y = y.divide(scale);
	  
        if (y.gt(ten)) {
            y = y.divide(ten);
            mag += 1;
        }
        else if (y.lt(ten)) {
            y = y.multiply(ten);
            mag -= 1;  	
        }
	  
        int decimalPointPos = mag + 1;
        StringBuffer buf = new StringBuffer();
        int numDigits = 32 - 1;
        for (int i = 0; i <= numDigits; i++) {
            if (insertDecimalPoint && i == decimalPointPos) {
                buf.append('.');
            }
            int digit = (int) y.hi;
            if (digit < 0) {
                break;
            }
            boolean rebiasBy10 = false;
            char digitChar = 0;
            if (digit > 9) {
                rebiasBy10 = true;
                digitChar = '9';
            }
	        else {
	            digitChar = (char) ('0' + digit);
	        }
            buf.append(digitChar);
	        y = (y.subtract(Quad.valueOf(digit)).multiply(ten));
	        if (rebiasBy10) y.sAdd(ten);
	        boolean continueExtractingDigits = true;
            int remMag = magnitude(y.hi);
            if (remMag < 0 && Math.abs(remMag) >= (numDigits - i)) continueExtractingDigits = false;
            if (! continueExtractingDigits) break;
	    }
        magnitude[0] = mag;
        return buf.toString();
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
}
