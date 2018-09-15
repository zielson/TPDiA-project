package model;

public class ReconciliationOutputLine implements Comparable<ReconciliationOutputLine> {

    private String dateFrom;
    private String dateTo;
    private double heightBeginning;
    private double heightEnding;
    private double reconcilation;
    private int tankNumber;
    private int spanIndex;
    private double heightMean;

    public ReconciliationOutputLine(String dateFrom, String dateTo, double heightBeginning, double heightEnding, double reconcilation, int tankNumber) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.heightBeginning = heightBeginning;
        this.heightEnding = heightEnding;
        this.reconcilation = reconcilation;
        this.tankNumber = tankNumber;
        this.spanIndex = 0;
        this.heightMean = (heightBeginning + heightEnding)/2.0;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public double getHeightBeginning() {
        return heightBeginning;
    }

    public double getHeightEnding() {
        return heightEnding;
    }

    public double getReconcilation() { return reconcilation; }

    public int getTankNumber() {
        return tankNumber;
    }

    public int getSpanIndex() { return spanIndex; }

    public void setSpanIndex(int index) { spanIndex = index; }

    public double getHeightMean() { return heightMean; }

    @Override
    public int compareTo(ReconciliationOutputLine o) {
        return 0;
    }
}
