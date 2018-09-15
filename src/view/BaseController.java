package view;

import javafx.scene.control.Alert;

public abstract class BaseController {

    protected void displayAlert(String message, String title, String header, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
