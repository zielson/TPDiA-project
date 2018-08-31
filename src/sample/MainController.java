package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;


public class MainController {

    @FXML private GridPane mainGridPane;
    @FXML private TextField filePathTextField;

    private FileChooser fileChooser;
    private File selectedFile;

    public MainController() {
        initializeFileChooser();
    }

    @FXML
    public void initialize() {
    }

    @FXML private void fileChooserButtonClicked() {
        File temporaryFile = fileChooser.showOpenDialog(mainGridPane.getScene().getWindow());
        if(temporaryFile != null) {
            filePathTextField.textProperty().set(temporaryFile.getPath());
            this.selectedFile = temporaryFile;
        }
    }

    private void initializeFileChooser()
    {
        this.fileChooser = new FileChooser();
        this.fileChooser.setTitle("Choose tank measurements data file");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(
            "*.txt, *.log", "*.txt", "*log");
        this.fileChooser.getExtensionFilters().add(extensionFilter);
    }
}
