package domain;

import model.*;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CurveCalibrationCorrector {

    private TankUtilities tankUtilities;
    private NozzleUtilities nozzleUtilities;
    private List<String> tankData;
    private List<String> nozzleData;
    private List<CurvePoint> originalCalibrationCurveDataset;
    private List<CurvePoint> correctedCalibrationCurveDataset;
    private List<ReconciliationOutputLine> reconcilationData;

    public CurveCalibrationCorrector() {
        this.tankUtilities = new TankUtilities();
        this.nozzleUtilities = new NozzleUtilities();
    }

    public void readFiles(File tankFile, File nozzleFile) throws  IOException {
        this.tankData = FileProcessing.readFile(tankFile);
        this.nozzleData = FileProcessing.readFile(nozzleFile);
    }

    public List<Integer> getTankCollection() {
        List<Integer> tankIdCollectionFromTankData = this.tankUtilities.getTankIdCollection(this.tankData);
        List<Integer> tankIdCollectionFromNozzleData = this.nozzleUtilities.getTankIdCollection(this.nozzleData);
        tankIdCollectionFromTankData.retainAll(tankIdCollectionFromNozzleData); //intersect
        return tankIdCollectionFromTankData;
    }

    public void adjustCalibrationCurve(int tankId) throws Exception
    {
        List<TankLine> tankLines = processTankMeasurementData(this.tankData, tankId);
        List<NozzleLine> nozzleLines = processNozzleMeasurementData(this.nozzleData, tankId);
        this.originalCalibrationCurveDataset = CurveUtilities.createOriginalCalibrationCurveDataset(tankLines);
        List<ReconciliationOutputLine> reconciliationOutputLines = calculateReconcilation(tankLines, nozzleLines, tankId);
        this.correctedCalibrationCurveDataset = calculateCorrectedCalibrationCurveDataset(reconciliationOutputLines, tankId);
        this.reconcilationData = reconciliationOutputLines.stream().sorted(Comparator.comparing(ReconciliationOutputLine::getDateFrom)).collect(Collectors.toList());
    }

    private List<ReconciliationOutputLine> calculateReconcilation(List<TankLine> tankLines, List<NozzleLine> nozzleLines, int tankId) throws Exception {
        List<ReconcilationInputLine> reconcilationInputLines = ReconcilationUtilities.prepareReconcilationInputDataset(tankLines, nozzleLines);
        List<ReconciliationOutputLine> reconciliationOutputLines = ReconcilationUtilities.calculateReconcilation(reconcilationInputLines, tankId);
        reconciliationOutputLines = reconciliationOutputLines.stream().sorted(Comparator.comparing(ReconciliationOutputLine::getHeightMean)).collect(Collectors.toList());
        return reconciliationOutputLines;
    }

    private List<CurvePoint> calculateCorrectedCalibrationCurveDataset(List<ReconciliationOutputLine> reconciliationOutputLines, int tankId) {
        double heightSpanWidth = 0.2; //temporary
        List<MeanHeightSpanWithMeanError> meanErrorList = ReconcilationUtilities.prepareDatasetWithMeanError(reconciliationOutputLines, heightSpanWidth);
        List<Point> errorPoints = ReconcilationUtilities.getErrorPoints(meanErrorList);
        List<SpanCharacteristic> spanCharacteristics = CurveUtilities.createCharacteristicsDataset(errorPoints);
        List<TankLine> originalCurveData = this.tankUtilities.getTankDataset(tankId);
        return CurveUtilities.createCorrectedCalibrationCurveDataset(originalCurveData, spanCharacteristics);
    }

    private List<TankLine> processTankMeasurementData(List<String> tankData, int tankId) {
        this.tankUtilities.processData(tankData, tankId);
        return this.tankUtilities.getTankDataset(tankId);
    }

    private List<NozzleLine> processNozzleMeasurementData(List<String> nozzleData, int tankId) {
        this.nozzleUtilities.processData(nozzleData, tankId);
        return this.nozzleUtilities.getNozzleDataset(tankId);
    }

    public List<CurvePoint> getOriginalCalibrationCurveDataset() {
        return originalCalibrationCurveDataset;
    }

    public List<CurvePoint> getCorrectedCalibrationCurveDataset() {
        return correctedCalibrationCurveDataset;
    }

    public List<ReconciliationOutputLine> getReconcilationData() {
        return  reconcilationData;
    }
}
