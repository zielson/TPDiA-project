package view;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import model.CurvePoint;

import java.util.List;

public class ChartUtilities {

    public static LineChart createLineChart(List<CurvePoint> dataset, String title, String xAxisLabel, String yAxisLabel) {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(xAxisLabel);
        yAxis.setLabel(yAxisLabel);
        LineChart<Double, Double> lineChart = new LineChart(xAxis, yAxis);
        lineChart.setTitle(title);
        XYChart.Series series = new XYChart.Series();
        dataset.forEach(data -> series.getData().add(new XYChart.Data(data.getHeight(), data.getVolume())));
        lineChart.getData().add(series);
        lineChart.setCreateSymbols(false);
        return lineChart;
    }
}
