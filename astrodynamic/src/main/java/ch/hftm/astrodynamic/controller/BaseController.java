package ch.hftm.astrodynamic.controller;

import java.io.IOException;
import java.util.Optional;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

// base controller implements reused methods
public abstract class BaseController {

    // used to setup javaFX window
    public abstract void initialize();

    protected abstract Stage getCurrentStage();

    // just show an error alert with errorMsg
    protected void showError(String errorMsg) {
        Alert alrt = new Alert(AlertType.ERROR);
        alrt.setContentText(errorMsg);
        alrt.show();
    }

    // simplified alert returns true if yes was chosen, all other options are false
    protected boolean askYesNo(String question) {
        Alert alrt = new Alert(AlertType.CONFIRMATION, question, ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alrt.showAndWait();

        if (result.get() == ButtonType.YES) {
            return true;
        } else {
            return false;
        }
    }

    // open new stage window to use
    protected Stage generateSubstage(String title, boolean canResize) {
        Stage substage = new Stage();
        substage.setTitle(title);
        substage.setResizable(canResize);
        return substage;
    }

    // load in a scene on the stage
    private void showSceneOnStage(Stage targetStage, String title, String sceneURI) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(BaseController.class.getResource(sceneURI));

            Scene scene = new Scene(loader.load());
            targetStage.setScene(scene);
            targetStage.setTitle(title);
            targetStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
