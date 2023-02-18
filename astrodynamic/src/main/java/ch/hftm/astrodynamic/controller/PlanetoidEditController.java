package ch.hftm.astrodynamic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// edit parameters of a planetoid
public class PlanetoidEditController extends BaseController{
    @FXML
    TextField posX;

    @FXML
    TextField posY;

    @FXML
    TextField posZ;

    @FXML
    TextField posMagnitude;

    @FXML
    TextField velX;

    @FXML
    TextField velY;

    @FXML
    TextField velZ;

    @FXML
    TextField velMagnitude;

    @FXML
    TextField name;

    @FXML
    TextField description;

    @FXML
    TextField mass;

    @FXML
    TextField zeroElevation;

    @FXML
    TextField atmosHeight;

    @FXML
    ComboBox atmosModel;

    @FXML
    CheckBox atmosOxygen;

    // now the unit combo boxes for convinient unit display
    @FXML
    ComboBox massUnit;

    @FXML
    ComboBox zeroElevationUnit;

    @FXML
    ComboBox positionUnit;

    @FXML
    ComboBox velocityUnit;

    @FXML
    ComboBox atmosHeightUnit;

    public PlanetoidEditController() {
        super();
    }

    @Override
    public void initialize(){
        showError("Test");
    }

    @FXML
    void onTestClicked(ActionEvent e) {
        askYesNo("You like tests?");
    }
}
