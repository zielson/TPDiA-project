package model;

import java.util.function.Function;

public class SpanCharacteristic {
    private double spanFrom;
    private double spanTo;
    private Function<Double, Double> characteristic;

    public SpanCharacteristic(double spanFrom, double spanTo, Function<Double, Double> characteristic) {
        this.spanFrom = spanFrom;
        this.spanTo = spanTo;
        this.characteristic = characteristic;
    }

    public double getSpanFrom() {
        return spanFrom;
    }

    public double getSpanTo() {
        return spanTo;
    }

    public Function<Double, Double> getCharacteristic() {
        return characteristic;
    }
}

