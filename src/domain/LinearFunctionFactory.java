package domain;

import model.Point;

import java.util.function.Function;

public class LinearFunctionFactory {

    public static Function<Double, Double> createLinearFunction(Point firstPoint, Point secondPoint) {
        //a-value
        double slope = (secondPoint.getY() - firstPoint.getY())/(secondPoint.getX() - firstPoint.getX());
        //b-value
        double yIntercept = firstPoint.getY() - slope * firstPoint.getX();
        return (x) -> slope * x + yIntercept;
    }
}
