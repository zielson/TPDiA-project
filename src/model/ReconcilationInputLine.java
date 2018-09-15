package model;

public class ReconcilationInputLine {
    private String measurementDate;
    private double tankHeight;
    private double tankVolume;
    private double nozzleVolume;

    public ReconcilationInputLine(String measurementDate, double tankHeight, double tankVolume, double nozzleVolume) {
        this.measurementDate = measurementDate;
        this.tankHeight = tankHeight;
        this.tankVolume = tankVolume;
        this.nozzleVolume = nozzleVolume;
    }

    public String getMeasurementDate() {
        return measurementDate;
    }

    public double getTankHeight() {
        return tankHeight;
    }

    public double getTankVolume() {
        return tankVolume;
    }

    public double getNozzleVolume() {
        return nozzleVolume;
    }
}
