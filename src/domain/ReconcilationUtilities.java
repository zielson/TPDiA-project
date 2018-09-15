package domain;

import model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ReconcilationUtilities {

    private static double getMaxHeigthValue(List<ReconciliationOutputLine> reconciliationFileLineList) {
        double maxHeightMean = 0.0;
        for(ReconciliationOutputLine line : reconciliationFileLineList) {
            if(line.getHeightMean() > maxHeightMean)
                maxHeightMean = line.getHeightMean();
        }
        return maxHeightMean;
    }

    public static List<ReconcilationInputLine> prepareReconcilationInputDataset(List<TankLine> tankLines, List<NozzleLine> nozzleLines) throws Exception {
        List<ReconcilationInputLine> reconcilationInput = new ArrayList<>();
        if(tankLines.size() != nozzleLines.size())
            throw new Exception("TankDataset and NozzleDataset have different number of instances");
        for(int i = 0; i < tankLines.size(); i++) {
            TankLine tankLine = tankLines.get(i);
            NozzleLine nozzleLine = nozzleLines.get(i);
            reconcilationInput.add(new ReconcilationInputLine(tankLine.getDate(),tankLine.getHeight(), tankLine.getVolume(), nozzleLine.getVolume()));
        }
        return reconcilationInput;
    }

    public static List<ReconciliationOutputLine> calculateReconcilation(List<ReconcilationInputLine> reconcilationInput, int tankId) {
        List<ReconciliationOutputLine> reconciliationOutput = new ArrayList<>();
        for(int i = 0; i < reconcilationInput.size() - 1; i++) {
            String beginningDate = reconcilationInput.get(i).getMeasurementDate();
            String endingDate = reconcilationInput.get(i+1).getMeasurementDate();
            double d1_V1 = reconcilationInput.get(i).getTankVolume();
            double d1_V2 = reconcilationInput.get(i+1).getTankVolume();
            double d2_V1 = reconcilationInput.get(i).getNozzleVolume();
            double d2_V2 = reconcilationInput.get(i+1).getNozzleVolume();
            double H1 = reconcilationInput.get(i).getTankHeight();
            double H2 = reconcilationInput.get(i+1).getTankHeight();

            double VAR1 = d1_V2 - d1_V1;
            double VAR2 = d2_V2 - d2_V1;
            double VAR = VAR1 - VAR2;

            ReconciliationOutputLine reconciliationOutputLine =
                    new ReconciliationOutputLine(beginningDate, endingDate, H1, H2, VAR, tankId);
            reconciliationOutput.add(reconciliationOutputLine);
        }
        return reconciliationOutput;
    }

    public static List<MeanHeightSpanWithMeanError> prepareDatasetWithMeanError(List<ReconciliationOutputLine> reconciliationOutputLines, double heightSpanWidth) {
        matchReconcilationLinesToHeightSpan(reconciliationOutputLines, heightSpanWidth);
        return calculateMeanErrorForEachHeightSpan(reconciliationOutputLines, heightSpanWidth);
    }

    private static int calculateHeightSpanCount(List<ReconciliationOutputLine> reconciliationOutputLines, double heightSpanWidth) {
        double maxHeightMean = getMaxHeigthValue(reconciliationOutputLines);
        return (int) Math.ceil(maxHeightMean / heightSpanWidth);
    }

    private static void matchReconcilationLinesToHeightSpan(List<ReconciliationOutputLine> reconciliationOutputLines, double heightSpanWidth) {
        int heightSpansCount = calculateHeightSpanCount(reconciliationOutputLines, heightSpanWidth);
        double currentSpanBeginningValue = 0.0;

        for(int i = 1; i <= heightSpansCount; i++) {
            for(ReconciliationOutputLine line : reconciliationOutputLines) {
                if(line.getHeightMean() > currentSpanBeginningValue && line.getHeightMean() <= (currentSpanBeginningValue + heightSpanWidth))
                    line.setSpanIndex(i);
            }
            currentSpanBeginningValue += heightSpanWidth;
        }
    }

    private static List<MeanHeightSpanWithMeanError> calculateMeanErrorForEachHeightSpan(List<ReconciliationOutputLine> reconciliationOutputLines, double heightSpanWidth) {
        List<MeanHeightSpanWithMeanError> finalDataList = new ArrayList<>();
        List<Point> errorPoints = new ArrayList<>();
        int heightSpansCount = calculateHeightSpanCount(reconciliationOutputLines, heightSpanWidth);
        //liczenie srednich bledow dla kazdego z przedzialow
        for(int i = 1; i <= heightSpansCount; i++)
        {
            //obliczenie i dodanie sredniej z przedzialow, np. prz: 0.5-0.10 -> 0.75
            double heightSpanMean = ((i - 1) * heightSpanWidth + i * heightSpanWidth) / 2;
            ArrayList<ReconciliationOutputLine> tempList = new ArrayList<>();
            for(ReconciliationOutputLine line : reconciliationOutputLines)
            {
                if(line.getSpanIndex() == i)
                    tempList.add(line);
            }
            double tempListSum = tempList.stream().mapToDouble(item -> item.getReconcilation()).sum();
            double meanError = tempListSum != 0 ? tempListSum / tempList.size() : 0;
            finalDataList.add(new MeanHeightSpanWithMeanError((i - 1)*heightSpanWidth,i * heightSpanWidth, heightSpanMean, meanError));
            errorPoints.add(new Point(heightSpanMean, meanError));
        }
        return finalDataList;
    }

    public static List<Point> getErrorPoints(List<MeanHeightSpanWithMeanError> meanErrorDataset) {
        List<Point> errorList = new ArrayList<>();
        meanErrorDataset.forEach(line -> errorList.add(new Point(line.getMeanHeightSpan(), line.getMeanError())));
        return  errorList;
    }
}
