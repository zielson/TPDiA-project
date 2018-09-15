package model;

public class TankLine {
    private String date;
    private double height;
    private double volume;
    private int tankId;

    public TankLine(String date, double height, double volume, int tankId) {
        this.date = date;
        this.height = height;
        this.volume = volume;
        this.tankId = tankId;
    }

    public String getDate() {
        return date;
    }

    public double getHeight() {
        return height;
    }

    public double getVolume() {
        return volume;
    }

    public int getTankId() {
        return tankId;
    }
}
