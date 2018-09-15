package model;

public class CurvePoint {

    private double height;
    private double volume;

    public CurvePoint(double height, double volume) {
        this.height = height;
        this.volume = volume;
    }

    public double getHeight() {
        return height;
    }

    public double getVolume() {
        return volume;
    }
}
