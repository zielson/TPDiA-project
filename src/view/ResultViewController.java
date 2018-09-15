package view;

import domain.CurveCalibrationCorrector;
import domain.FileProcessing;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import model.CurvePoint;

import java.io.File;
import java.util.List;

public class ResultViewController extends BaseController {

    @FXML AnchorPane calibrationCurveBeforeAnchorPane;
    @FXML AnchorPane reconcilationCurveAnchorPane;
    @FXML AnchorPane calibrationCurveAfterAnchorPane;
    @FXML VBox mainVBox;

    private CurveCalibrationCorrector curveCalibrationCorrector;
    private List<CurvePoint> correctedCurveDataset;
    private List<CurvePoint> originalCurveDataset;
    private List<CurvePoint> reconciliationCurveDataset;

    private FileChooser reconciliationFileChooser;
    private FileChooser correctedCurveDatasetFileChooser;

    public ResultViewController() {
    }

    @FXML
    public void initialize() {
        this.reconciliationFileChooser = createFileChooser("Save Reconciliation Data","*.csv");
        this.correctedCurveDatasetFileChooser = createFileChooser("Save Corrected Curve Dataset","*.csv");
    }

    public void setCurveCalibrationCorrector(CurveCalibrationCorrector curveCalibrationCorrector) {
        this.curveCalibrationCorrector = curveCalibrationCorrector;
    }

    public void initCorrectedCurveChart(List<CurvePoint> correctedCurveDataset, int tankId) {
        this.correctedCurveDataset = correctedCurveDataset;
        LineChart lineChart = ChartUtilities.createLineChart(
                correctedCurveDataset, String.format("Corrected calibration curve - tank %d", tankId), "Height [m]", "Volume [m^3]");
        lineChart.prefWidthProperty().bind(calibrationCurveAfterAnchorPane.prefWidthProperty());
        lineChart.prefHeightProperty().bind(calibrationCurveAfterAnchorPane.prefHeightProperty());
        this.calibrationCurveAfterAnchorPane.getChildren().add(lineChart);
    }

    public void initOriginalCurveChart(List<CurvePoint> originalCurveDataset, int tankId) {
        this.originalCurveDataset = originalCurveDataset;
        LineChart lineChart = ChartUtilities.createLineChart(
                originalCurveDataset, String.format("Original calibration curve - tank %d", tankId), "Height [m]", "Volume [m^3]");
        lineChart.prefWidthProperty().bind(calibrationCurveBeforeAnchorPane.prefWidthProperty());
        lineChart.prefHeightProperty().bind(calibrationCurveBeforeAnchorPane.prefHeightProperty());
        this.calibrationCurveBeforeAnchorPane.getChildren().add(lineChart);
    }

    @FXML private void saveCorrectedCurveDatasetClicked() {
        File file = this.correctedCurveDatasetFileChooser.showSaveDialog(mainVBox.getScene().getWindow());
        if(file != null) {
            try {
                FileProcessing.writeCorrectedCalibrationCurveDatasetToFile(this.curveCalibrationCorrector.getCorrectedCalibrationCurveDataset(), file);
            }
            catch (Exception e) {
                e.printStackTrace();
                displayAlert(e.getMessage(), "Error", "Error occurred:", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML private void saveReconciliationDataClicked() {
        File file = this.reconciliationFileChooser.showSaveDialog(mainVBox.getScene().getWindow());
        if(file != null) {
            try {
                FileProcessing.writeReconciliationDatasetToFile(this.curveCalibrationCorrector.getReconcilationData(), file);
            }
            catch (Exception e) {
                e.printStackTrace();
                displayAlert(e.getMessage(), "Error", "Error occurred:", Alert.AlertType.ERROR);
            }
        }
    }

    private FileChooser createFileChooser(String title, String extension)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
                extension, extension);
        fileChooser.getExtensionFilters().add(extensionFilter);
        return fileChooser;
    }
}
