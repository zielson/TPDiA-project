package model;

public class NozzleLine {
    private String date;
    private double volume;
    private int tankId;

    public NozzleLine(String date, double volume, int tankId) {
        this.date = date;
        this.volume = volume;
        this.tankId = tankId;
    }

    public String getDate() {
        return date;
    }

    public double getVolume() {
        return volume;
    }

    public int getTankId() {
        return tankId;
    }
}
