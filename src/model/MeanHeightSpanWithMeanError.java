package model;

public class MeanHeightSpanWithMeanError {

    private double heightFrom;
    private double heightTo;
    private double meanHeightSpan;
    private double meanError;

    public MeanHeightSpanWithMeanError(double heightFrom, double heightTo, double meanHeightSpan, double meanError)
    {
        this.heightFrom = heightFrom;
        this.heightTo = heightTo;
        this.meanHeightSpan = meanHeightSpan;
        this.meanError = meanError;
    }

    public double getMeanHeightSpan() { return meanHeightSpan; }
    public double getMeanError() { return meanError; }

    public double getHeightFrom() {
        return heightFrom;
    }

    public double getHeightTo() {
        return heightTo;
    }
}
