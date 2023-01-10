package ch.hftm.astrodynamic.utils;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

public class AreaScalar extends BaseScalar {

    public AreaScalar(Scalar scalar) {
        super(scalar, Unit.AREA);
    }

    public AreaScalar(Quad value) {
        super(value, Unit.AREA);
    }

    public AreaScalar(int value) {
        super(value, Unit.AREA);
    }

    public AreaScalar(double value) {
        super(value, Unit.AREA);
    }

    @Override
    public Scalar multiply(Scalar scalar) throws UnitConversionError {
        throw new UnitConversionError(String.format("Area can not be multiplied by %s", scalar.getUnit().toString() ));
    }

    @Override
    public Scalar divide(Scalar scalar) throws UnitConversionError {
        Quad divisionResult = this.getValue().divide(scalar.getValue());
        switch(scalar.getUnit()) {
            case LENGTH:
                return new LengthScalar(divisionResult);
            default:
                throw new UnitConversionError(String.format("Area can not be divided by %s", scalar.getUnit().toString() ));
        }
    }

    // Calculates the Area of a Circle
    public static AreaScalar circleArea(LengthScalar r) {
        Quad result = Quad.PI.multiply(r.getValue().pow(2));
        return new AreaScalar(result);
    }

    // Calculates the Area of a Triangle
    public static AreaScalar triangleArea(LengthScalar base, LengthScalar height) {
        Quad result = base.getValue().multiply(height.getValue()).divide(new Quad(2));
        return new AreaScalar(result);
    }

    // Calculate the Area of a square
    public static AreaScalar squareArea(LengthScalar side) {
        return rectangleArea(side, side);
    }

    // Calculate the Area of a rectangle
    public static AreaScalar rectangleArea(LengthScalar length, LengthScalar width) {
        return new AreaScalar(length.getValue().multiply(width.getValue()));
    }
    
    // Calculate the Area of a paralellogram
    public static AreaScalar paralellogramArea(LengthScalar base, LengthScalar height) {
        return rectangleArea(base, height);
    }

    // Calculate the Area of a trapezium
    // a and b are lenghts of the paralell sides
    // h is perpendicular height of the trapezium
    public static AreaScalar trapeziumArea(LengthScalar a, LengthScalar b, LengthScalar h) {
        Quad result = a.getValue().add(b.getValue()).multiply(h.getValue()).divide(new Quad(2));
        return new AreaScalar(result);
    }

}
