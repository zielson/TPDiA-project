package domain;

import model.CurvePoint;
import model.Point;
import model.SpanCharacteristic;
import model.TankLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class CurveUtilities {

    //ustawianie przedziałow z odpowiednimi funkcjami liniowymi
    public static List<SpanCharacteristic> createCharacteristicsDataset(List<Point> errorPoints) {
        List<SpanCharacteristic> spanCharacteristics = new ArrayList<>();
        for(int i = 0; i < errorPoints.size() - 1; i++)
        {
            double spanFrom = errorPoints.get(i).getX();
            double spanTo = errorPoints.get(i + 1).getX();
            Function<Double, Double> characteristic = LinearFunctionFactory.createLinearFunction(errorPoints.get(i), errorPoints.get(i + 1));
            spanCharacteristics.add(new SpanCharacteristic(spanFrom, spanTo, characteristic));
        }
        return spanCharacteristics;
    }

    public static List<CurvePoint> createCorrectedCalibrationCurveDataset(List<TankLine> originalCurveDataset, List<SpanCharacteristic> spanCharacteristics) {
        List<CurvePoint> correctedCurveDataset = new ArrayList<>();
        for(TankLine curveDataLine : originalCurveDataset) {
            double height = curveDataLine.getHeight();
            double volume = curveDataLine.getVolume();
            Optional<SpanCharacteristic> characteristic = spanCharacteristics.stream()
                    .filter(item -> height > item.getSpanFrom() && height <= item.getSpanTo()).findFirst();

            if(!characteristic.isPresent())
            {
                SpanCharacteristic firstCharacteristic = spanCharacteristics.get(0);
                SpanCharacteristic lastCharacteristic = spanCharacteristics.get(spanCharacteristics.size() - 1);
                double distanceFromBeginning = Math.abs(height - firstCharacteristic.getSpanFrom());
                double distanceFromEnding = Math.abs(height - lastCharacteristic.getSpanTo());
                characteristic = Optional.of(distanceFromBeginning < distanceFromEnding ? firstCharacteristic : lastCharacteristic);
            }
            double correctedVolume = volume - characteristic.get().getCharacteristic().apply(height);
            correctedCurveDataset.add(new CurvePoint(height, correctedVolume));
        }
        return correctedCurveDataset;
    }

    public static List<CurvePoint> createOriginalCalibrationCurveDataset(List<TankLine> originalCurveDataset) {
        List<CurvePoint> calibrationCurveDataset = new ArrayList<>();
        originalCurveDataset.forEach(line -> calibrationCurveDataset.add(new CurvePoint(line.getHeight(), line.getVolume())));
        return calibrationCurveDataset;
    }
}
