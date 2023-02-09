package ch.hftm.astrodynamic;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import ch.hftm.astrodynamic.utils.ConfigRepository;

// Taken from OOP1, Creates a stage for the first window, after that the controllers are self-governing
public class MainApp extends Application {
    private Stage primaryStage;

    public static void main(String[] args) {
        // Register args in Configuration repository
        ConfigRepository.registerArgs(args);
        // Launch application with args
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Astrodynamic");
        this.showTestView();
    }

    public void showTestView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/MissionView.fxml"));

            // Show the scene containing the root layout.
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.toString());
        }
    }
}
