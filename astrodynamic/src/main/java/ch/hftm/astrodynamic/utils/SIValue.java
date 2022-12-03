package ch.hftm.astrodynamic.utils;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

 // Converter for SI units
public class SIValue {

    private Quad num;  // Number to be stored in this object
    private SIUnit unit; // Unit to be stored in this object

    // Constructor for doubles
    public SIValue(Quad num, SIUnit unit) {
        this.num = num;
        this.unit = unit;
    }

    // Constructor for int
    public SIValue(int num, SIUnit unit) {
        this.num = new Quad(num);
        this.unit = unit;
    }

    // Constructor for double
    public SIValue(double num, SIUnit unit) {
        this.num = new Quad(num);
        this.unit = unit;
    }

    // Getter for number stored in this object
    public Quad get() { return this.num; }
    public void set(Quad num) { this.num = num; }
    public int getInt() { return this.get().toInt(); }
    public double getDouble() { return this.get().toDouble(); }

    // Getter for SI unit
    public SIUnit getUnit() { return this.unit; }

}
