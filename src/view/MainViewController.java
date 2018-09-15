package view;

import domain.CurveCalibrationCorrector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class MainViewController extends BaseController {

    @FXML private VBox mainBox;
    @FXML private Button startPreprocessingButton;
    @FXML private Button tankFileChooserButton;
    @FXML private Button nozzleFileChooserButton;
    @FXML private Button startProcessButton;


    @FXML private TextField tankFilePathTextField;
    @FXML private TextField nozzleFilePathTextField;
    @FXML private HBox radioButtonsHBox;
    @FXML private VBox panelVBox;

    private FileChooser tankFileChooser;
    private FileChooser nozzleFileChooser;
    private File tankFile;
    private File nozzleFile;
    private CurveCalibrationCorrector curveCalibrationCorrector;
    private ToggleGroup radioButtonGroup;
    private int currentTankId;

    public MainViewController() {
        this.tankFileChooser = createFileChooser("Choose tank measurements log file");
        this.nozzleFileChooser = createFileChooser("Choose nozzle measurements log file");
    }

    @FXML
    public void initialize() {
        this.startPreprocessingButton.prefWidthProperty().bind(tankFilePathTextField.widthProperty());
        this.startProcessButton.prefWidthProperty().bind(panelVBox.widthProperty());
        this.radioButtonGroup = new ToggleGroup();
    }

    @FXML private void tankFileChooserButtonClicked() {
        File temporaryFile = tankFileChooser.showOpenDialog(mainBox.getScene().getWindow());
        if(temporaryFile != null) {
            tankFilePathTextField.textProperty().set(temporaryFile.getPath());
            this.tankFile = temporaryFile;
        }
    }

    @FXML private void nozzleFileChooserButtonClicked() {
        File temporaryFile = nozzleFileChooser.showOpenDialog(mainBox.getScene().getWindow());
        if(temporaryFile != null) {
            nozzleFilePathTextField.textProperty().set(temporaryFile.getPath());
            this.nozzleFile = temporaryFile;
        }
    }

    @FXML private void startProcessButtonClicked() {
        try {
            currentTankId = Integer.parseInt(((RadioButton)radioButtonGroup.getSelectedToggle()).textProperty().getValue());
            curveCalibrationCorrector.adjustCalibrationCurve(currentTankId);
            openResultView();
        }
        catch (Exception e){
            e.printStackTrace();
            displayAlert(e.getMessage(), "Error", "Error occurred:", Alert.AlertType.ERROR);
        }
    }

    private FileChooser createFileChooser(String title)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
            "*.txt, *.log", "*.txt", "*log");
        fileChooser.getExtensionFilters().add(extensionFilter);
        return fileChooser;
    }

    @FXML private void startPreprocessingButtonClicked() {
        if(tankFile ==  null || nozzleFile == null)
            displayAlert("Both files should be selected!", "Error", "Error occurred:", Alert.AlertType.ERROR);
        else {
            try {
                curveCalibrationCorrector = new CurveCalibrationCorrector();
                curveCalibrationCorrector.readFiles(tankFile, nozzleFile);
                List<Integer> tankIdList = curveCalibrationCorrector.getTankCollection();

                tankIdList.forEach(tankId -> {
                    RadioButton radioButton = new RadioButton(tankId.toString());
                    radioButton.setToggleGroup(radioButtonGroup);
                    radioButtonsHBox.getChildren().add(radioButton);
                });
                RadioButton firstRadioButton = (RadioButton)radioButtonsHBox.getChildren().get(0);
                firstRadioButton.selectedProperty().setValue(true);
                panelVBox.visibleProperty().setValue(true);
            }
            catch (Exception e) {
                e.printStackTrace();
                displayAlert(e.getMessage(), "Error", "Error occurred:", Alert.AlertType.ERROR);
            }
        }
    }

    private void openResultView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ResultView.fxml"));
        Parent root1 = (Parent)fxmlLoader.load();
        ResultViewController controller = fxmlLoader.getController();
        controller.setCurveCalibrationCorrector(this.curveCalibrationCorrector);
        controller.initOriginalCurveChart(this.curveCalibrationCorrector.getOriginalCalibrationCurveDataset(), this.currentTankId);
        controller.initCorrectedCurveChart(this.curveCalibrationCorrector.getCorrectedCalibrationCurveDataset(), this.currentTankId);
        Stage stage = new Stage();
        stage.setTitle("Results");
        stage.setScene(new Scene(root1));
        stage.showAndWait();
    }

    @FXML private void helpClicked() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("1. Select tank and nozzle measurements files");
        stringBuilder.append('\n');
        stringBuilder.append("2. Click \"Preprocess Files\"");
        stringBuilder.append('\n');
        stringBuilder.append("3. Choose appropriate tank Id to analyze");
        stringBuilder.append('\n');
        stringBuilder.append("4. Click \"Start\" to begin process");
        displayAlert(stringBuilder.toString(), "Info", "How to use:", Alert.AlertType.INFORMATION);
    }
}
