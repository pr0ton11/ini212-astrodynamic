package ch.hftm.astrodynamic;

import java.io.IOException;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import ch.hftm.astrodynamic.utils.ConfigRepository;
import ch.hftm.astrodynamic.utils.Log;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// Taken from OOP1, Creates a stage for the first window, after that the controllers are self-governing
public class MainApp extends Application {

    private Logger log = Log.build();

    private Stage primaryStage;

    public static void main(String[] args) {
        // Register args in Configuration repository
        ConfigRepository.registerArgs(args);
        // Launch application with args
        launch(args);
    }

    // launches primary window
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Astrodynamic");
        this.showMissionView();
    }

    // opens up the mission overview
    public void showMissionView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/MissionView.fxml"));

            // Show the scene containing the root layout.
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            log.severe(e.getMessage());
        }
    }
}
