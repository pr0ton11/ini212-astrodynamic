package ch.hftm.astrodynamic.utils;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

 // Converter for SI units
public class SIValue {

    private double num;  // Number to be stored in this object
    private SIUnit unit; // Unit to be stored in this object

    // Constructor for doubles
    public SIValue(double num, SIUnit unit) {
        this.num = num;
        this.unit = unit;
    }

    // Constructor for int
    public SIValue(int num, SIUnit unit) {
        this.num = (double) num;
        this.unit = unit;
    }

    // Getter for number stored in this object
    public double get() { return this.num; }
    public int getInt() { return (int)this.num; }

    // Getter for SI unit
    public SIUnit getUnit() { return this.unit; }

    // Check if unit is time based
    public boolean isTimeUnit() {
        return this.unit.equals(SIUnit.TIME);
    }

    // Setting the time (seconds) 
    public void setTime(double seconds) {
        if (isTimeUnit()) {
            this.num = seconds;
        } else {
            throw new SIConversionError("Unit is not time based");
        }
    }

    // Getting the time (seconds)
    public double getTime() {
        if (isTimeUnit()) {
            return this.num;
        } else {
            throw new SIConversionError("Unit is not time based");
        }
    }

    // Setting the time (minutes)
    public void setMinutes(double minutes) {
        setTime(minutes * 60);
    }

    // Getting the time (minutes)
    public double getMinutes() {
        return getTime() / 60;
    }

    // Setting the time (hours)
    public void setHours(double hours) { 
        setMinutes(hours * 60);
    }

    // Getting the time (hours)
    public double getHours() {
        return getMinutes() / 60;
    }

    // Setting the time (days)
    public void setDays(double days) {
        setHours(days * 24);
    }

    // Getting the time (days)
    public double getDays() {
        return getHours() / 24;
    }

    // Setting the time (weeks)
    public void setWeeks(double weeks) {
        setDays(weeks * 7);
    }

    // Getting the time (weeks)
    public double getWeeks() {
        return getDays() / 7;
    }

    // Setting the time (months)
    public void setMonths(double months) {
        // TODO: Do we use 30 days for months?
        setDays(months * 30);
    }

    // Getting the time (months) 
    public double getMonths() {
        // TODO: Do we use 30 days for months?
        return getDays() / 30;
    }

    // Setting the time (years)
    public void setYears(double years) {
        setDays(years * 365);
    }

    // Getting the time (years) 
    public double getYears() {
        return getDays() / 365;
    }

}
