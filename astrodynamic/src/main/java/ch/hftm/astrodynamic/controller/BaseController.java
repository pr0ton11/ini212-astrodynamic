package ch.hftm.astrodynamic.controller;

import java.io.IOException;
import java.util.Optional;

import ch.hftm.astrodynamic.MainApp;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

// base controller implements reused methods
public abstract class BaseController {

    // used to setup javaFX window
    public abstract void initialize();

    protected Stage getCurrentStage(Event e) {
        return (Stage)((Node)e.getSource()).getScene().getWindow();
    }

    // just show an error alert with errorMsg
    protected void showError(String errorMsg) {
        Alert alrt = new Alert(AlertType.ERROR);
        alrt.setContentText(errorMsg);
        alrt.show();
    }

    // just show an information
    protected void showInfo(String infoMsg) {
        Alert alrt = new Alert(AlertType.INFORMATION);
        alrt.setContentText(infoMsg);
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
    protected void showSceneOnStage(Stage targetStage, String sceneURI) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(sceneURI)); // load from MainApp to have correct depth for views directory

            Scene scene = new Scene(loader.load());
            targetStage.setScene(scene);
            targetStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // load in a scene on a new stage
    protected void showSceneOnNewStage(String title, boolean canResize, String sceneURI) {
        Stage substage = generateSubstage(title, canResize);
        showSceneOnStage(substage, sceneURI);
    }
}
